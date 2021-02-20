package com.castprogramms.balamutbatut.tools

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.gson.GsonBuilder

class DataUserFirebase: DataUserApi {

    private val gsonConverter = GsonBuilder().create()
    val fireStore = FirebaseFirestore.getInstance()

    override fun addStudent(student: Student, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .set(student)
    }

    override fun updateStudent(student: Student, studentID: String) {
        
    }

    override fun deleteStudent(student: Student) {
        fireStore.collection(studentTag)
                .document()
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
            .addSnapshotListener { value, error ->
                try {
                    value?.documents?.forEach {
                        this@DataUserFirebase.mutableListGroups.add(
                            gsonConverter.fromJson(
                                it.data.toString(),
                                Group::class.java
                            )
                        )
                    }
                    mutableLiveDataGroups.postValue(this@DataUserFirebase.mutableListGroups)
                }catch (e: Exception){
                    printLog(error?.message.toString())
                }
            }
    }

    fun getStudent(studentID: String): Task<DocumentSnapshot> {
        return fireStore.collection(studentTag)
            .document(studentID)
            .get()
    }

    fun getStudentsGroup(studentID: String): Task<QuerySnapshot> {
        return fireStore.collection(groupTag)
            .whereArrayContains(studentTag, studentID)
            .get()
    }
    fun updateNodeStudent(studentID: String, nodes:List<Node>){
        if (User.student != null){
            val diff = nodes.filter { !User.student?.nodes?.contains(it)!! }
            printLog(diff.toString())
        }
    }

    companion object{
        const val studentTag = "students"
        const val groupTag = "groups"
        fun printLog(message: String){
            Log.e("Test", message)
        }
    }
}