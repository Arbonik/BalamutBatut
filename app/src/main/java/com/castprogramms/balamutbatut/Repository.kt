package com.castprogramms.balamutbatut

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson

class Repository(private val dataUserFirebase: DataUserFirebase) {

    private val _userData = MutableLiveData<Resource<out Person>>(null)

    val user: LiveData<Resource<out Person>> = _userData

    fun loadUserData(account: GoogleSignInAccount?) {
        _userData.postValue(Resource.Loading())
        if (account != null) {
            var person = Person()
            val id = account.id.toString()
            User.id = id


            dataUserFirebase.getUser(id).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result
                    if (data != null && data.data != null) {
                        person.type = data.getString("type").toString()
                        DataUserFirebase.printLog(data.data.toString())
                        User.mutableLiveDataSuccess.postValue(true)
                        when (person.type) {
                            TypesUser.STUDENT.desc -> {
                                User.typeUser = TypesUser.STUDENT
                                person = data.toObject(Student::class.java)!!
                                User.setValueStudent(person as Student)
                            }
                            TypesUser.TRAINER.desc -> {
                                User.typeUser = TypesUser.TRAINER
                                person = Gson().fromJson(data.data.toString(), Trainer::class.java)
                                User.setValueTrainer(person as Trainer)
                            }
                        }
                        person.img = account.photoUrl.toString()
                        DataUserFirebase.printLog(person.first_name)
                        _userData.postValue(Resource.Success(person))

                    }
                } else {
                    _userData.postValue(Resource.Error(""))
                }
            }.continueWith {
                if (User.student != null) {
                    dataUserFirebase.getStudentsGroup(User.id).addOnSuccessListener {
                        if (it.documents.isNotEmpty()) {
                            User.setValueStudent(User.student?.apply {
                                groupID = it.documents.first().getString("name").toString()
                            }!!)
                        }
                    }
                }
            }
                .addOnFailureListener {

                    _userData.postValue(Resource.Error("Вы не зарегестрированы"))
                }
        }


    }

    fun getGroup(groupID: String) = dataUserFirebase.getGroup(groupID)
    fun getStudent(studentID: String) = dataUserFirebase.getUser(studentID)
    fun getElement(elements: List<Element>) = dataUserFirebase.getElement(elements)
    fun getAllElements(idElements: Array<String>) = dataUserFirebase.getElements(idElements)
    fun updateElementsStudent(element: Element, studentID: String) =
        dataUserFirebase.addStudentElement(element, studentID)
    fun updateUserFirstName(first_name: String, studentID: String) =
        dataUserFirebase.editNameStudent(first_name, studentID)
    fun updateUserSecondName(second_name: String, studentID: String) =
        dataUserFirebase.editLastNameStudent(second_name, studentID)
    fun updateUserIcon(img: String, studentID: String) =
        dataUserFirebase.editIconStudent(img, studentID)
}