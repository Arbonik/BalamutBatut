package com.castprogramms.balamutbatut.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.BuildConfig
import com.castprogramms.balamutbatut.tools.EditProfile
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.tools.User
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

    init {
        val options = FirebaseOptions.Builder()
            .setApplicationId(BuildConfig.APP_ID)
            .setApiKey(BuildConfig.API_FIRE_KEY)
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
        fireStore.runTransaction {
            fireStore.collection(studentTag)
                .document(studentID)
                .set(student)

            if (student.referId != ""){
                addBatutCoin(50, studentID)
                addBatutCoin(50, student.referId)
            }
        }
    }

    override fun addBatutCoin(quantity: Int, id: String) {
        fireStore.collection(studentTag)
            .document(id)
            .update("batutcoin", FieldValue.increment(1 * quantity.toDouble()))
    }

    override fun writeOffCoin(quantity: Int, id: String): MutableLiveData<Resource<Boolean>> {
        val mutableLiveData = MutableLiveData<Resource<Boolean>>(null)
        var isValidate = false
        mutableLiveData.postValue(Resource.Loading())
        fireStore.collection(studentTag)
            .document(id)
            .get()
            .addOnSuccessListener {
                Log.e("data", it.getLong("batutcoin").toString())
                if (it.getLong("batutcoin") != null) {
                    val quantityCoin = it.getLong("batutcoin")!!.toInt()
                    isValidate = quantityCoin >= quantity
                }
            }.continueWith {
                if (isValidate) {
                    mutableLiveData.postValue(Resource.Success(true))
                    fireStore.collection(studentTag)
                        .document(id)
                        .update("batutcoin", FieldValue.increment(-1 * quantity.toDouble()))
                } else
                    mutableLiveData.postValue(Resource.Error("У пользователя недостаточно средств"))
            }
        return mutableLiveData
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
            .document(trueOrder)
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

    fun getAllStudents(): MutableLiveData<Resource<List<Pair<String, Student>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<List<Pair<String, Student>>>>(Resource.Loading())
        fireStore.collection(studentTag)
            .whereEqualTo(EditProfile.TYPE.desc, "student")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val pairs = mutableListOf<Pair<String, Student>>()
                    value.documents.forEach {
                        pairs.add(it.id to it.toObject(Student::class.java)!!)
                    }
                    mutableLiveData.postValue(Resource.Success(pairs))
                } else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
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

    fun getCollectionAllStudents(groupID: String):
            MutableLiveData<Resource<MutableList<Pair<String, Student>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<String, Student>>>>(Resource.Loading())
        fireStore.collection(studentTag)
            .whereEqualTo("type", "student")
            .whereEqualTo("groupID", groupID)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val idsAndStudents = mutableListOf<Pair<String, Student>>()
                    value.documents.forEach {
                        if (it.toObject(Student::class.java) != null) {
                            idsAndStudents.add(it.id to it.toObject(Student::class.java)!!)
                        }
                    }
                    mutableLiveData.postValue(Resource.Success(idsAndStudents))
                } else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    fun getCollectionAllStudentsWithoutGroup(): MutableLiveData<Resource<MutableList<Pair<String, Student>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<String, Student>>>>(Resource.Loading())
        fireStore.collection(studentTag)
            .whereEqualTo("groupID", Person.notGroup)
            .whereEqualTo("type", "student")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val idAndStudent = mutableListOf<Pair<String, Student>>()
                    value.documents.forEach {
                        if (it.toObject(Student::class.java) != null)
                            idAndStudent.add(it.id to it.toObject(Student::class.java)!!)
                    }
                    mutableLiveData.postValue(Resource.Success(idAndStudent))
                } else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
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

    override fun getGroups(): MutableLiveData<Resource<MutableList<Pair<Group, String>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<Group, String>>>>(Resource.Loading())
        fireStore.collection(groupTag)
            .whereEqualTo("numberTrainer", User.id)
            .addSnapshotListener { value, error ->
                val groupAndId = mutableListOf<Pair<Group, String>>()
                if (value != null) {
                    value.documents.forEach {
                        groupAndId.add((it.toObject(Group::class.java) to it.id) as Pair<Group, String>)
                    }
                    mutableLiveData.postValue(Resource.Success(groupAndId))
                } else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
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

    fun getNameGroup(groupID: String): Task<DocumentSnapshot> {
        return fireStore.collection(groupTag)
            .document(groupID)
            .get()
    }

    fun getGroup(groupID: String): MutableLiveData<Resource<Group>> {
        val mutableLiveData = MutableLiveData<Resource<Group>>(Resource.Loading())
        var name = ""
        fireStore.collection(groupTag)
            .whereEqualTo("name", groupID)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty())
                    name = it.documents.first().id
            }.continueWith {
                fireStore.collection(groupTag)
                    .document(name)
                    .addSnapshotListener { value, error ->
                        if (value?.toObject(Group::class.java) != null)
                            mutableLiveData.postValue(
                                Resource.Success(value.toObject(Group::class.java)!!)
                            )
                        else
                            mutableLiveData.postValue(Resource.Error(error?.message))
                    }
            }
        return mutableLiveData
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
                Log.e("size", it.documents.size.toString())
                it.documents.forEach {
                    Log.e("doc", it.data.toString())
                    if (it.id != trueOrder) {
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

    fun getSortedElements(IDS: Map<String, List<Int>>): MutableLiveData<MutableList<Pair<String, List<Element>>>> {
        val allElements = mutableMapOf<String, List<Element>>()
        val mutableLiveDataAllElements = MutableLiveData<MutableList<Pair<String, List<Element>>>>()
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                Log.e("size", it.documents.size.toString())
                it.documents.forEach {
                    Log.e("doc", it.data.toString())
                    if (it.id != trueOrder) {
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
            }.continueWith {
                var trueOrderList = listOf<String>()
                fireStore.collection(elementTag)
                    .document(trueOrder)
                    .get()
                    .addOnSuccessListener {
                        trueOrderList = it["names"] as List<String>
                        mutableLiveDataAllElements.postValue(
                            filter(trueOrderList, allElements)
                        )
                    }
            }
        return mutableLiveDataAllElements
    }

    fun getAddSortedElementsWithColor(IDS: Map<String, List<Int>>): MutableLiveData<Resource<MutableList<Pair<String, Int>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<String, Int>>>>(Resource.Loading())
        var titleAndColor = mutableListOf<Pair<String, Int>>()
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                titleAndColor = mutableListOf()
                it.documents.forEach {
                    if (it.id != trueOrder) {
                        val elements = mutableListOf<Element>()
                        val names = it.get("name") as List<String>
                        for (i in names.indices) {
                            if (IDS[it.id] != null) {
                                if (i !in IDS[it.id]!!) {
                                    elements.add(Element(names[i]))
                                }
                            }
                            else {
                                elements.add(Element(names[i]))
                            }
                        }
                        val sendElements = elements.toList()
                        if (sendElements.isNotEmpty())
                            titleAndColor.add(it.id to it.getLong("color")!!.toInt())
                        elements.clear()
                    }
//                    Log.e("it", it.data.toString())
//                    if (it.id != trueOrder)
//                        titleAndColor.add(it.id to it.getLong("color")!!.toInt())
                }
            }.addOnFailureListener {
                mutableLiveData.postValue(Resource.Error(it.message))
            }.continueWith {
                var trueOrderList = listOf<String>()
                fireStore.collection(elementTag)
                    .document(trueOrder)
                    .get()
                    .addOnSuccessListener {
                        trueOrderList = it["names"] as List<String>
                        mutableLiveData.postValue(
                            Resource.Success(filter(trueOrderList, titleAndColor))
                        )
                    }
            }
        return mutableLiveData
    }

    fun getSortedElementsWithColor(IDS: Map<String, List<Int>>): MutableLiveData<Resource<MutableList<Pair<String, Int>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<String, Int>>>>(Resource.Loading())
        var titleAndColor = mutableListOf<Pair<String, Int>>()
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                titleAndColor = mutableListOf()
                it.documents.forEach {
                    if (it.id != trueOrder) {
                        val elements = mutableListOf<Element>()
                        val names = it.get("name") as List<String>
                        for (i in names.indices) {
                            if (IDS[it.id] != null)
                                if (i in IDS[it.id]!!)
                                    elements.add(Element(names[i]))
                        }
                        val sendElements = elements.toList()
                        if (sendElements.isNotEmpty())
                            titleAndColor.add(it.id to it.getLong("color")!!.toInt())
                        elements.clear()
                    }
//                    Log.e("it", it.data.toString())
//                    if (it.id != trueOrder)
//                        titleAndColor.add(it.id to it.getLong("color")!!.toInt())
                }
            }.addOnFailureListener {
                mutableLiveData.postValue(Resource.Error(it.message))
            }.continueWith {
                var trueOrderList = listOf<String>()
                fireStore.collection(elementTag)
                    .document(trueOrder)
                    .get()
                    .addOnSuccessListener {
                        trueOrderList = it["names"] as List<String>
                        mutableLiveData.postValue(
                            Resource.Success(filter(trueOrderList, titleAndColor))
                        )
                    }
            }
        return mutableLiveData
    }

    fun getElementStudent(studentID: String): MutableLiveData<Resource<MutableList<Pair<String, List<Int>>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<MutableList<Pair<String, List<Int>>>>>(Resource.Loading())
        fireStore.collection(studentTag)
            .document(studentID)
            .addSnapshotListener { value, error ->
                if (value != null)
                    mutableLiveData.postValue(
                        Resource.Success(
                            (value.get("element") as Map<String, List<Int>>).toList()
                                .toMutableList()
                        )
                    )
                else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    fun getRang(id: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        val elements = mutableMapOf<String, List<Element>>()
        var trueOrderList = listOf<String>()
        fireStore.collection(elementTag)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    if (it.id != trueOrder) {
                        val names = it.get("name") as List<String>
                        val currentElements = mutableListOf<Element>()
                        names.forEach { currentElements.add(Element(it)) }
                        elements.put(it.id, currentElements)
                    } else
                        trueOrderList = it.get("names") as List<String>
                }
            }.continueWith {
                fireStore.collection(studentTag)
                    .document(id)
                    .addSnapshotListener { value, error ->
                        if (value != null && value.getString("type") == "student") {
                            val student = value.toObject(Student::class.java)
                            if (student != null)
                                mutableLiveData.postValue(
                                    Resource.Success(
                                        checkRang(
                                            filter(
                                                trueOrderList,
                                                elements
                                            ).toMap().toMutableMap(), student.element
                                        )
                                    )
                                )
                        } else
                            mutableLiveData.postValue(Resource.Error(error?.message))
                    }
            }
        return mutableLiveData
    }

    fun getStudentsOfGroup(groupID: String): MutableLiveData<Resource<List<Pair<String, Student>>>> {
        val mutableLiveData =
            MutableLiveData<Resource<List<Pair<String, Student>>>>(Resource.Loading())
        var name = ""
        fireStore.collection(groupTag)
            .whereEqualTo("name", groupID)
            .get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty())
                    name = it.documents.first().id
            }.continueWith {
                fireStore.collection(studentTag)
                    .whereEqualTo("groupID", name)
                    .whereEqualTo("type", "student")
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                            val studentAndId = mutableListOf<Pair<String, Student>>()
                            value.documents.forEach {
                                studentAndId.add(it.id to it.toObject(Student::class.java)!!)
                            }
                            mutableLiveData.postValue(
                                Resource.Success(
                                    studentAndId
                                )
                            )
                        } else
                            mutableLiveData.postValue(Resource.Error(error?.message))
                    }
            }
        return mutableLiveData
    }

    override fun updateDataGroup(group: Group, groupID: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.collection(groupTag)
            .document(groupID)
            .set(group).addOnCompleteListener {
                if (it.isSuccessful)
                    mutableLiveData.postValue(Resource.Success("Данные успешно обновлены"))
                else
                    if (it.isCanceled)
                        mutableLiveData.postValue(Resource.Error(it.exception?.message))
            }
        return mutableLiveData
    }

    fun getAddStudentElementsOnThisTitle(
        idStudent: String,
        title: String
    ): MutableLiveData<Resource<MutableList<Element>>> {
        val mutableLiveData = MutableLiveData<Resource<MutableList<Element>>>(Resource.Loading())
        var noNeedPositionElements = listOf<Int>()
        val mutableListNeedElements = mutableListOf<Element>()
        fireStore.collection(studentTag)
            .document(idStudent)
            .get()
            .addOnSuccessListener {
                val student = it.toObject(Student::class.java)
                if (student != null) {
                    noNeedPositionElements =
                        if (student.element.containsKey(title))
                            student.element[title]!!
                        else
                            listOf()
                }
            }.addOnFailureListener {
                mutableLiveData.postValue(Resource.Error(it.message))
            }.continueWith {
                fireStore.collection(elementTag)
                    .document(title)
                    .get()
                    .addOnSuccessListener {
                        val listElements = it.get("name") as List<String>
                        listElements.forEachIndexed { index, s ->
                            if (index !in noNeedPositionElements)
                                mutableListNeedElements.add(Element(s))
                        }
                        mutableLiveData.postValue(Resource.Success(mutableListNeedElements))
                    }
            }
        return mutableLiveData
    }

    fun getStudentElementsOnThisTitle(
        idStudent: String,
        title: String
    ): MutableLiveData<Resource<MutableList<Element>>> {
        val mutableLiveData = MutableLiveData<Resource<MutableList<Element>>>(Resource.Loading())
        var needPositionElements = listOf<Int>()
        val mutableListNeedElements = mutableListOf<Element>()
        fireStore.collection(studentTag)
            .document(idStudent)
            .get()
            .addOnSuccessListener {
                val student = it.toObject(Student::class.java)
                if (student != null) {
                    needPositionElements =
                        if (student.element.containsKey(title))
                            student.element[title]!!
                        else
                            listOf()
                }
            }.addOnFailureListener {
                mutableLiveData.postValue(Resource.Error(it.message))
            }.continueWith {
                fireStore.collection(elementTag)
                    .document(title)
                    .get()
                    .addOnSuccessListener {
                        val listElements = it.get("name") as List<String>
                        listElements.forEachIndexed { index, s ->
                            if (index in needPositionElements)
                                mutableListNeedElements.add(Element(s))
                        }
                        mutableLiveData.postValue(Resource.Success(mutableListNeedElements))
                    }
            }
        return mutableLiveData
    }

    fun getAllElementsOnThisTitle(title: String): MutableLiveData<Resource<MutableList<Element>>> {
        val mutableLiveData = MutableLiveData<Resource<MutableList<Element>>>(Resource.Loading())
        fireStore.collection(elementTag)
            .document(title)
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val list = value.get("name") as List<String>
                    val elements = mutableListOf<Element>()
                    list.forEach {
                        elements.add(Element(it))
                    }
                    mutableLiveData.postValue(Resource.Success(elements))
                } else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    fun getGroupOnID(groupID: String): MutableLiveData<Resource<Group>> {
        val mutableLiveData = MutableLiveData<Resource<Group>>(Resource.Loading())
        fireStore.collection(groupTag)
            .document(groupID)
            .addSnapshotListener { value, error ->
                if (value?.toObject(Group::class.java) != null)
                    mutableLiveData.postValue(
                        Resource.Success(value.toObject(Group::class.java)!!)
                    )
                else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    fun getPlaceStudentInRating(studentID: String): MutableLiveData<Resource<Int>> {
        val mutableLiveData = MutableLiveData<Resource<Int>>(Resource.Loading())
        fireStore.collection(studentTag)
            .whereEqualTo("type", "student")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    val studentsAndId = mutableListOf<Pair<String, Student>>()
                    value.documents.forEach {
                        studentsAndId.add(it.id to it.toObject(Student::class.java)!!)
                    }
                    studentsAndId.sortByDescending {
                        it.second.getQuantityElements().split(" ")[1].toInt()
                    }
                    var position = -1
                    studentsAndId.forEachIndexed { index, pair ->
                        if (pair.first == studentID)
                            position = index + 1
                    }
                    if (position != -1)
                        mutableLiveData.postValue(Resource.Success(position))
                    else
                        mutableLiveData.postValue(Resource.Error("Нет такого ученика"))
                } else {
                    mutableLiveData.postValue(Resource.Error(error?.message))
                }
            }
        return mutableLiveData
    }

    fun getFullNameTrainer(trainerID: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.collection(studentTag)
            .document(trainerID)
            .addSnapshotListener { value, error ->
                if (value?.toObject(Trainer::class.java) != null)
                    mutableLiveData.postValue(
                        Resource.Success(value.toObject(Trainer::class.java)!!.getFullName())
                    )
                else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }

        return mutableLiveData
    }

    fun deleteGroup(groupID: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>()
        fireStore.runTransaction {
            fireStore.collection(studentTag)
                .whereEqualTo("groupID", groupID)
                .get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        fireStore.collection(studentTag)
                            .document(it.id)
                            .update("groupID", "НетГруппы")
                    }
                }.continueWith {
                    fireStore.collection(groupTag)
                        .document(groupID)
                        .delete()
                }
        }.addOnSuccessListener {
            mutableLiveData.postValue(Resource.Success("Удаление прошло успешно"))
        }.addOnFailureListener {
            mutableLiveData.postValue(Resource.Error(it.message))
        }

        return mutableLiveData
    }

    fun deleteGroup(group: Group): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>()
        fireStore.runTransaction {
            var groupID = ""
            fireStore.collection(groupTag)
                .whereEqualTo("color", group.color)
                .whereEqualTo("description", group.description)
                .whereEqualTo("name", group.name)
                .whereEqualTo("students", group.students)
                .whereEqualTo("numberTrainer", group.numberTrainer)
                .get().addOnSuccessListener {
                    if (it.documents.first() != null)
                        groupID = it.documents.first().id
                }
            if (groupID != "") {
                fireStore.collection(groupTag)
                    .document(groupID)
                    .delete()
                val removeID = mutableListOf<String>()

                fireStore.collection(studentTag)
                    .whereEqualTo("groupID", groupID)
                    .get()
                    .addOnSuccessListener {
                        it.documents.forEach {
                            removeID.add(it.id)
                        }
                    }

                removeID.forEach {
                    fireStore.collection(studentTag)
                        .document(it)
                        .update("groupID", Person.notGroup)
                }
            }
        }.addOnSuccessListener {
            mutableLiveData.postValue(Resource.Success("Удаление прошло успешно"))
        }.addOnFailureListener {
            mutableLiveData.postValue(Resource.Error(it.message))
        }

        return mutableLiveData
    }

    companion object {
        const val elementTag = "elements"
        const val studentTag = "students"
        const val groupTag = "groups"
        const val trueOrder = "TRUEORDER"

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
                map[s] = index
            }
            return map
        }

        fun checkRang(
            allElements: MutableMap<String, List<Element>>,
            studentElements: Map<String, List<Int>>
        ): String {
            var rang = "Нет ранга"
            for (i in studentElements) {
                if (allElements.containsKey(i.key)) {
                    if (allElements[i.key]?.size == studentElements[i.key]?.size)
                        rang = i.key
                }
            }
            return rang
        }

        private fun filter(
            trueOrderList: List<String>,
            elements: Map<String, List<Element>>
        ): MutableList<Pair<String, List<Element>>> {
            val list =
                MutableList<Pair<String, List<Element>>>(trueOrderList.size) { Pair("", listOf()) }
            val map = getListAndPosition(trueOrderList)
            elements.forEach {
                list[map[it.key]!!] = it.toPair()
            }
            for (i in list.indices.reversed())
                if (list[i].first == "" && list[i].second.isEmpty())
                    list.removeAt(i)
            return list
        }

        private fun filter(
            trueOrderList: List<String>,
            elements: MutableList<Pair<String, Int>>
        ): MutableList<Pair<String, Int>> {
            val list =
                MutableList<Pair<String, Int>>(trueOrderList.size) { Pair("", 0) }
            val map = getListAndPosition(trueOrderList)
            elements.forEach {
                list[map[it.first]!!] = it
            }
            for (i in list.indices.reversed())
                if (list[i].first == "" && list[i].second == 0)
                    list.removeAt(i)
            return list
        }
    }
}