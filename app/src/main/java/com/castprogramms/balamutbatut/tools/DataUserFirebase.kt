package com.castprogramms.balamutbatut.tools

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
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

    override fun addTrainer(trainer: Trainer, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .set(trainer)
    }

    override fun deleteStudentFromGroup(studentID: String, groupID: String){
        fireStore.collection(studentTag)
            .document(studentID)
            .update(EditProfile.GROUP.desc, Person.notGroup)
        fireStore.collection(groupTag)
            .document(groupID)
            .update("students", FieldValue.arrayRemove(studentID))
    }

    override fun editNameStudent(first_name: String, studentID: String){
        fireStore.collection(studentTag)
            .document(studentID)
            .update(EditProfile.FIRST_NAME.desc, first_name)
    }

    override fun editLastNameStudent(second_name: String, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .update(EditProfile.SECOND_NAME.desc, second_name)
    }

    override fun editIconStudent(icon: String, studentID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .update(EditProfile.IMG.desc, icon)
    }
    fun addStudentElement(element: Element, studentID: String) {
        var id = ""
        Log.e("id", element.name)
        fireStore.collection(elementTag)
            .whereEqualTo("name", element.name)
            .get()
            .addOnSuccessListener {
                Log.e("id", it.documents.size.toString())
                if (it.documents.isNotEmpty()) {
                    id = it.documents.first().id
                }
            }.continueWith {
                Log.e("id", id)
                if (id != "") {
                    fireStore.collection(studentTag)
                        .document(studentID)
                        .update(EditProfile.ELEMENTS.desc, FieldValue.arrayUnion(Element(id)))
                }
            }
    }

    override fun updateStudent(studentID: String, groupID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .update("groupID", groupID)

        fireStore.collection(groupTag)
            .document(groupID)
            .update("students", FieldValue.arrayUnion(studentID))
    }

    override fun deleteStudent(student: Student) {
        fireStore.collection(studentTag)
                .document()
                .delete()
    }

    fun getCollectionAllStudents(groupID: String): Query {
        return fireStore.collection(studentTag)
            .whereEqualTo("type", "student")
            .whereEqualTo("groupID", groupID)
    }

    fun getCollectionAllStudentsWithoutGroup(): Query {
        return fireStore.collection(studentTag)
            .whereEqualTo("groupID", Person.notGroup)
            .whereEqualTo("type", "student")
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

    fun getGroup(groupID: String): DocumentReference {
        return fireStore.collection(groupTag)
            .document(groupID)
    }


    fun getElement(elements: List<Element>): MutableLiveData<MutableList<Element>> {
        val listElements = mutableListOf<Element>()
        val mutableLiveDataElements = MutableLiveData(listElements)
        elements.forEach {
            fireStore.collection(elementTag)
                .document(it.name)
                .get()
                .addOnSuccessListener {
                    it.toObject(Element::class.java)?.let { it1 -> listElements.add(it1) }
                    mutableLiveDataElements.postValue(listElements)
                }
        }
        return mutableLiveDataElements
    }

    fun getElements(idElements: Array<String>): MutableLiveData<MutableList<Element>>{
        val allElements = mutableListOf<Element>()
        val mutableLiveDataAllElements = MutableLiveData(allElements)
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    if (!idElements.contains(it.id)) {
                        it.toObject(Element::class.java)
                            ?.let { element -> allElements.add(element) }
                        mutableLiveDataAllElements.postValue(allElements)
                    }
                }
            }
        return mutableLiveDataAllElements
    }

    companion object{
        const val elementTag = "elements"
        const val studentTag = "students"
        const val groupTag = "groups"
        fun printLog(message: String){
            Log.e("Test", message)
        }
    }
}

