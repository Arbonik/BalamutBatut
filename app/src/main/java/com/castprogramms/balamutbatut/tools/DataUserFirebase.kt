package com.castprogramms.balamutbatut.tools

import android.util.Log
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.gson.GsonBuilder

class DataUserFirebase: DataUserApi {

    private val gsonConverter = GsonBuilder().create()
    val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()
    val fireStore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = settings
    }

    override fun addStudent(student: Student, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .set(student)
    }

    override fun updateStudent(student: Student, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .update("nameGroup", student.groupID)

        fireStore.collection(groupTag)
            .document(student.groupID)
            .update("students", FieldValue.arrayUnion(studentID))
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

    override fun getGroups(): Query {
        return fireStore.collection(groupTag)
            .whereEqualTo("numberTrainer", User.id)
    }

    fun getUser(studentID: String): DocumentReference {
        return fireStore.collection(studentTag)
            .document(studentID)
    }

    fun getStudentsGroup(studentID: String): Task<QuerySnapshot> {
        return fireStore.collection(groupTag)
            .whereArrayContains(studentTag, studentID)
            .get()
    }
    fun updateNodeStudent(studentID: String, nodes:List<Node>){
        fireStore.collection(studentTag)
            .document(studentID)
            .update("nodes", nodes)
    }
    fun getNameGroup(groupID: String): DocumentReference {
        return fireStore.collection(groupTag)
            .document(groupID)
    }
    fun getGroupStudents(group: Group){
//        return fireStore.collection(groupTag)
//            .where
    }

    companion object{
        const val studentTag = "students"
        const val groupTag = "groups"
        fun printLog(message: String){
            Log.e("Test", message)
        }
    }
}