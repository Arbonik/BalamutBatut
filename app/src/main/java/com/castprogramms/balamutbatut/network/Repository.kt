package com.castprogramms.balamutbatut.network

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class Repository(
    private val dataUserFirebase: DataUserFirebase,
    private val videoAndDescFirebaseStorage: VideoAndDescFirebaseStorage
) {

    private val _userData = MutableLiveData<Resource<out Person>>(null)

    val user: LiveData<Resource<out Person>> = _userData

    fun loadUserData(account: GoogleSignInAccount?): Boolean {
        _userData.postValue(Resource.Loading())
        if (account != null) {
            var person = Person()
            val id = account.id.toString()
            User.id = id
            dataUserFirebase.getUser(id).addSnapshotListener { value, error ->
                if (value != null) {
                    if (value.data != null) {
                        DataUserFirebase.printLog(value.data.toString())
                        person.type = value.getString("type").toString()
                        DataUserFirebase.printLog(person.first_name)
                        User.mutableLiveDataSuccess.postValue(true)
                        when (person.type) {
                            TypesUser.STUDENT.desc -> {
                                User.typeUser = TypesUser.STUDENT
                                person = value.toObject(Student::class.java)!!
                                DataUserFirebase.printLog(person.toString())
                                User.setValueStudent(person as Student)
                                if (User.student != null) {
                                    dataUserFirebase.getStudentsGroup(User.id)
                                        .addOnSuccessListener {
                                            if (it.documents.isNotEmpty()) {
                                                User.setValueStudent(User.student?.apply {
                                                    groupID = it.documents.first().getString("name")
                                                        .toString()
                                                }!!)
                                            }
                                        }
                                }
                            }
                            TypesUser.TRAINER.desc -> {
                                User.typeUser = TypesUser.TRAINER
                                person = value.toObject(Trainer::class.java)!!
                                User.setValueTrainer(person as Trainer)
                            }
                        }
                        _userData.postValue(Resource.Success(person))

                    } else {
                        User.mutableLiveDataSuccess.postValue(false)
                        _userData.postValue(Resource.Error(error?.message.toString()))
                    }
                } else {
                    DataUserFirebase.printLog(error?.message.toString())
                    _userData.postValue(Resource.Error(error?.message.toString()))
                }
            }
            return true
        } else
            return false
    }

    fun loadUserData(id: String) {
        dataUserFirebase.getUser(id).addSnapshotListener { value, error ->
            var person = Person()
            if (value != null) {
                val data = value
                if (data.data != null) {
                    person.type = data.getString("type").toString()
                    User.mutableLiveDataSuccess.postValue(true)
                    DataUserFirebase.printLog(data.data.toString())
                    when (person.type) {
                        TypesUser.STUDENT.desc -> {
                            User.typeUser = TypesUser.STUDENT
                            person = data.toObject(Student::class.java)!!
                            DataUserFirebase.printLog(person.toString())
                            User.setValueStudent(person as Student)
                            if (User.student != null) {
                                dataUserFirebase.getStudentsGroup(User.id).addOnSuccessListener {
                                    if (it.documents.isNotEmpty()) {
                                        User.setValueStudent(User.student?.apply {
                                            groupID =
                                                it.documents.first().getString("name").toString()
                                        }!!)
                                    }
                                }
                            }
                        }
                        TypesUser.TRAINER.desc -> {
                            User.typeUser = TypesUser.TRAINER
                            person = data.toObject(Trainer::class.java)!!
                            User.setValueTrainer(person as Trainer)
                        }
                    }
                }
            } else {
                _userData.postValue(Resource.Error(error?.message.toString()))
            }
        }
    }

    fun deleteGroup(groupID: String) = dataUserFirebase.deleteGroup(groupID)
    fun addGroup(group: Group) = dataUserFirebase.addGroup(group)
    fun getGroup(groupID: String) = dataUserFirebase.getGroup(groupID)
    fun getGroupInfo(groupID: String) = dataUserFirebase.getGroupOnID(groupID)
    fun getStudent(studentID: String) = dataUserFirebase.getUser(studentID)
    fun getElement(IDs: List<Int>, nameGroupElement: String) =
        dataUserFirebase.getElement(IDs, nameGroupElement)

    fun getAllElements(idElements: Map<String, List<Int>>) =
        dataUserFirebase.getElements(idElements)

    fun getAllSortedElements(idElements: Map<String, List<Int>>) =
        dataUserFirebase.getSortedElements(idElements)

    fun getStudentTitleElementsWithColor(idElements: Map<String, List<Int>>) =
        dataUserFirebase.getSortedElementsWithColor(idElements)

    fun getStudentElements(studentID: String) = dataUserFirebase.getElementStudent(studentID)
    fun updateElementsStudent(elements: Map<String, List<Element>>, studentID: String) =
        dataUserFirebase.addStudentElement(elements, studentID)

    fun updateUserFirstName(first_name: String, studentID: String) =
        dataUserFirebase.editNameStudent(first_name, studentID)

    fun getFullNameTrainer(trainerID: String) = dataUserFirebase.getFullNameTrainer(trainerID)
    fun updateUserSecondName(second_name: String, studentID: String) =
        dataUserFirebase.editLastNameStudent(second_name, studentID)

    fun getStudentsOfGroup(groupID: String) = dataUserFirebase.getStudentsOfGroup(groupID)
    fun getAddStudentElementsOnThisTitle(idStudent: String, title: String) =
        dataUserFirebase.getAddStudentElementsOnThisTitle(idStudent, title)

    fun getAllElementsOnThisTitle(title: String) = dataUserFirebase.getAllElementsOnThisTitle(title)
    fun updateUserIcon(img: String, studentID: String) =
        dataUserFirebase.editIconStudent(img, studentID)

    fun updateDataGroup(group: Group, groupID: String) =
        dataUserFirebase.updateDataGroup(group, groupID)

    fun getPlaceStudentInRating(studentID: String) =
        dataUserFirebase.getPlaceStudentInRating(studentID)

    fun loadPhotoUser(uri: Uri, userId: String) =
        videoAndDescFirebaseStorage.loadPhotoUser(uri, userId)

    fun deleteStudentFromGroup(studentID: String, groupID: String) =
        dataUserFirebase.deleteStudentFromGroup(studentID, groupID)

    fun getCollectionAllStudent(groupID: String) =
        dataUserFirebase.getCollectionAllStudents(groupID)

    fun getCollectionAllStudentsWithoutGroup() =
        dataUserFirebase.getCollectionAllStudentsWithoutGroup()

    fun updateStudentGroup(studentID: String, groupID: String) =
        dataUserFirebase.updateStudent(studentID, groupID)

    fun getAllStudents() = dataUserFirebase.getAllStudents()
    fun getTrueOrder() = dataUserFirebase.getTrueOrder()
    fun getGroups() = dataUserFirebase.getGroups()
    fun getNameGroup(groupID: String) = dataUserFirebase.getNameGroup(groupID)
    fun addStudent(student: Student, id: String) = dataUserFirebase.addStudent(student, id)
    fun addTrainer(trainer: Trainer, id: String) = dataUserFirebase.addTrainer(trainer, id)
    fun addBatutCoin(id: String, quantity: Int) = dataUserFirebase.addBatutCoin(quantity, id)
    fun writeOffCoin(id: String, quantity: Int) = dataUserFirebase.writeOffCoin(quantity, id)

    fun getRang(id: String) = dataUserFirebase.getRang(id)

    //video
    fun loadVideo(video: Uri, idVideo: String) =
        videoAndDescFirebaseStorage.loadVideo(video, idVideo)

    fun downloadVideo(title: String, name: String) =
        videoAndDescFirebaseStorage.downloadVideo(title, name)

    //desc
    fun loadDesc(desc: String, idVideo: String) =
        videoAndDescFirebaseStorage.loadDesc(desc, idVideo)

    fun downloadDescAndLevel(title: String, name: String) =
        videoAndDescFirebaseStorage.downloadDescAndLevel(title, name)

    fun loadVideoNameDecs(
        titleElement: String, nameElement: String, video: Uri,
        desc: String, level: String
    ) = videoAndDescFirebaseStorage
        .loadVideoNameDecs(titleElement, nameElement, video, desc, level)

    fun checkHaveVideo(title: String, name: String) =
        videoAndDescFirebaseStorage.haveVideo(title, name)

}
