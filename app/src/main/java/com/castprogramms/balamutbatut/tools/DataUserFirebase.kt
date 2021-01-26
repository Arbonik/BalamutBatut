package com.castprogramms.balamutbatut.tools

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.GsonBuilder

class DataUserFirebase: DataUserApi {

    private val gsonConverter = GsonBuilder().create()
    val fireStore = FirebaseFirestore.getInstance()

    override fun addStudent(student: Student, group: Group) {
        fireStore.collection(groupTag)
            .document()
            .collection(studentTag)
            .add(student)
    }

    override fun updateStudent(student: Student) {
        
    }

    override fun deleteStudent(student: Student) {
        fireStore.collection(studentTag)
                .document(student.number)
                .delete()
    }

    override fun readAllStudent(group: Group): MutableList<Student> {
        val mutableListStudents = mutableListOf<Student>()
        fireStore.collection(groupTag)
            .document()
            .collection(studentTag)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    task.result?.documents?.forEach {
                        mutableListStudents.add(
                            gsonConverter.fromJson(it.data.toString(), Student::class.java)
                        )
                    }
                }
                else
                    printLog(task.exception?.message.toString())
            }
        return mutableListStudents
    }

    override fun addGroup(group: Group) {
        fireStore.collection(groupTag)
            .add(group)
    }

    private val mutableListGroups = mutableListOf<Group>()
    private val mutableLiveDataGroups = MutableLiveData<MutableList<Group>>(mutableListGroups)

    fun getAllGroup():MutableLiveData<MutableList<Group>>{
        getGroups()
        return mutableLiveDataGroups
    }

    override fun getGroups(){
        fireStore.collection(groupTag)
            .addSnapshotListener(object :EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    value?.documents?.forEach {
                        this@DataUserFirebase.mutableListGroups.add(gsonConverter.fromJson(it.data.toString(), Group::class.java))
                    }
                    mutableLiveDataGroups.postValue(this@DataUserFirebase.mutableListGroups)
                }
            })
    }

    companion object{
        const val studentTag = "students"
        const val groupTag = "groups"
        fun printLog(message: String){
            Log.e("Test", message)
        }
    }
}