package com.castprogramms.balamutbatut.tools

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.BuildConfig
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.*
import com.google.gson.GsonBuilder

class DataUserFirebase(val applicationContext: Context) : DataUserApi {

    private val gsonConverter = GsonBuilder().create()
    val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()

    //val file = FileInputStream("google-services-test.json")
    init {
        val options = FirebaseOptions.Builder()
            .setApplicationId(BuildConfig.APP_ID)
            .setApiKey(BuildConfig.API_KEY)
            .setProjectId(BuildConfig.PROJECT_ID)
            .setStorageBucket(BuildConfig.STORAGE_BUCKET)
            .build()
        FirebaseApp.initializeApp(applicationContext, options, "test")
    }

    val fireStore = FirebaseFirestore.getInstance(
        FirebaseApp.getInstance("test")
    ).apply {
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

    override fun deleteStudentFromGroup(studentID: String, groupID: String) {
        fireStore.collection(studentTag)
            .document(studentID)
            .update(EditProfile.GROUP.desc, Person.notGroup)
        fireStore.collection(groupTag)
            .document(groupID)
            .update("students", FieldValue.arrayRemove(studentID))
    }

    override fun getTrueOrder(): MutableLiveData<Resource<List<String>>> {
        val mutableLiveData = MutableLiveData<Resource<List<String>>>(Resource.Loading())
        fireStore.collection(elementTag)
            .document("TRUEORDER")
            .addSnapshotListener { value, error ->
                if (value?.get("names") != null)
                    mutableLiveData.postValue(Resource.Success(value.get("names") as List<String>))
                else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    override fun editNameStudent(first_name: String, studentID: String) {
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

    fun getAllStudents(): Query {
        return fireStore.collection(studentTag)
            .whereEqualTo(EditProfile.TYPE.desc, "student")

    }

    fun addStudentElement(elements: Map<String, List<Element>>, studentID: String) {
        elements.forEach {
            fireStore.collection(elementTag)
                .document(it.key)
                .get()
                .addOnSuccessListener { it1 ->
                    val elements = it1["name"] as List<String>
                    it.value.forEach { it2 ->
                        val position = checkInArray(elements, it2)
                        if (position >= 0) {
                            fireStore.collection(studentTag)
                                .document(studentID)
                                .update("element.${it.key}", FieldValue.arrayUnion(position))
                        }
                    }
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
                if (task.isSuccessful) {
                    task.result?.documents?.forEach {
                        mutableListStudents.add(
                            gsonConverter.fromJson(it.data.toString(), Student::class.java)
                        )
                    }
                } else
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

    fun updateNodeStudent(studentID: String, nodes: List<Node>) {
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


    fun getElement(
        IDs: List<Int>,
        nameGroupElement: String
    ): MutableLiveData<MutableList<Element>> {
        var listElements = mutableListOf<Element>()
        val mutableLiveDataElements = MutableLiveData(listElements)
        fireStore.collection(elementTag)
            .document(nameGroupElement)
            .get()
            .addOnSuccessListener {
                val names = it.get("name") as List<String>
                for (i in names.indices) {
                    if (i in IDs) {
                        listElements.add(Element(names[i]))
                    }
                }
                mutableLiveDataElements.postValue(listElements)
            }
            .continueWith {
                fireStore.collection(elementTag)
                    .document("TRUEORDER")
                    .get()
                    .addOnSuccessListener {
                        if (it.get("name") != null) {
                            val listNames = (it.get("names") as List<String>).toMutableList()
                            val list = MutableList<Element>(listNames.size) { Element("") }
                            val map = getListAndPosition(listNames)
                            listElements.forEach {
                                list[map[it.name]!!] = it
                            }
                            for (i in list.indices.reversed())
                                if (list[i].name == "")
                                    list.removeAt(i)
                            listElements = list

                        }
                    }
            }
        return mutableLiveDataElements
    }

    fun getElements(IDS: Map<String, List<Int>>): MutableLiveData<Map<String, List<Element>>> {
        val allElements = mutableMapOf<String, List<Element>>()
        val mutableLiveDataAllElements = MutableLiveData(mapOf<String, List<Element>>())
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    if (it.id != "TRUEORDER") {
                        val elements = mutableListOf<Element>()
                        val names = it.get("name") as List<String>
                        for (i in names.indices) {
                            if (IDS[it.id] != null) {
                                if (i !in IDS[it.id]!!) {
                                    elements.add(Element(names[i]))
                                }
                            } else {
                                elements.add(Element(names[i]))
                            }
                        }
                        val sendElements = elements.toList()
                        allElements.put(it.id, sendElements)
                        elements.clear()
                    }
                }
                mutableLiveDataAllElements.postValue(allElements.toMap())
            }
        return mutableLiveDataAllElements
    }

    fun addData() {
        getElements(mapOf()).observeForever {
            val options = FirebaseOptions.Builder()
                .setApplicationId("1:763812191636:android:980e70a174428e33fd0381")
                .setApiKey("AIzaSyBKZw180EP0RhPvkoNPsgpbCNv9eNWugbs")
                .setProjectId("testbalamutbatut")
                .setStorageBucket("gs://testbalamutbatut.appspot.com")
                .build()
//            FirebaseApp.initializeApp(applicationContext, options, "test1")
            val fire = FirebaseFirestore.getInstance(
                FirebaseApp.initializeApp(
                    applicationContext,
                    options,
                    "dfjsk"
                )
            ).apply {
                firestoreSettings = settings
            }
            it.forEach {
                fire.collection(elementTag)
                    .document(it.key)
                    .set(it.value)
            }
        }
    }

    companion object {
        const val elementTag = "elements"
        const val studentTag = "students"
        const val groupTag = "groups"
        fun printLog(message: String) {
            Log.e("Test", message)
        }

        fun checkInArray(list: List<String>, element: Element): Int {
            var position = -1
            for (i in list.indices) {
                if (list[i] == element.name) {
                    position = i
                    break
                }
            }
            return position
        }

        fun getListAndPosition(list: List<String>): MutableMap<String, Int> {
            val map = mutableMapOf<String, Int>()
            list.forEachIndexed { index, s ->
                map.put(s, index)
            }
            return map
        }
    }
}

