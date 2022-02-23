package com.sdn.teampredators.polima.utils

import com.sdn.teampredators.polima.ui.home.model.Politician
import com.sdn.teampredators.polima.ui.home.model.Promise
import com.sdn.teampredators.polima.ui.home.model.PromiseStatus
import java.util.*
import kotlin.random.Random

object DummyPoliticalData {
    private val firstNames = listOf(
        "Mike",
        "Uche",
        "Joe",
        "Mark",
        "Chinedu",
        "Priye",
        "John",
        "Chike",
        "Adam",
        "Boma"
    )
    private val lastNames = listOf(
        "Reginald",
        "Pepple",
        "Dagogo",
        "Hart",
        "Dappa",
        "Green",
        "Jack",
        "Imoke",
        "Emeregini",
        "Dan-Jumbo"
    )

    private fun getFullName(): String {
        val randomFirstName = firstNames[Random.nextInt(firstNames.size)]
        val randomLastName = lastNames[Random.nextInt(lastNames.size)]
        return randomFirstName.plus(" $randomLastName")
    }

    private val politicalParties = listOf(
        "APC", "PDP", "APGA", "ADC", "APP", "AA", "PRP", "ADP", "APM", "LP", "SDP"
    )

    private fun getPoliticalParty(): String {
        return politicalParties[Random.nextInt(politicalParties.size)]
    }

    private val imageUrls = listOf(
        "https://1.bp.blogspot.com/-x4NzuML1R2o/W6YasJf4sOI/AAAAAAAAp48/1EC1gg3uIPEHX6zmZuQyzRDz-IbJ8rPcACLcBGAs/s1600/IMG_5080%2B%25281%2529.JPG",
        "https://upload.wikimedia.org/wikipedia/commons/5/51/Muhammadu_Buhari%2C_President_of_the_Federal_Republic_of_Nigeria_%28cropped3%29.jpg",
        "https://images.unsplash.com/photo-1533108344127-a586d2b02479?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8bmlnZXJpYW4lMjBtYW58ZW58MHx8MHx8&w=1000&q=80",
        "https://c.files.bbci.co.uk/assets/ca4dbacb-12b5-46f7-aa88-b52be8cfe8e6",
        "https://nigerianinfopedia.com.ng/wp-content/uploads/2020/03/Ayo-Fayose-powerful-politician.jpg",
        "https://nigerianfinder.com/wp-content/uploads/2017/12/tony-elumelu-net-worth.png",
        "https://media.kanyidaily.com/2020/12/11151604/Femi-Adesina.jpeg",
        "https://www.currentschoolnews.com/wp-content/uploads/2020/03/Rochas-Okorocha.jpg",
        "https://guardian.ng/wp-content/uploads/2021/07/Umahi-1-1062x598.jpg",
        "https://www.informationng.com/wp-content/uploads/2017/10/saidu-600x583.jpg"
    )

    private val projectUrls = listOf(
        "https://prod-001.s3.amazonaws.com/media/articleImages/Roads.jpg",
        "https://storage.googleapis.com/thisday-846548948316-wp-data/wp-media/2019/11/a8f395bf-ferma.jpg",
        "https://mawafd.org/wp-content/uploads/2021/01/Screenshot_20210118-151812.png",
        "https://guardian.ng/wp-content/uploads/2016/06/hospital-640x360.jpg",
        "https://www.vanguardngr.com/wp-content/uploads/2018/04/Test-WATER.jpg",
        "https://nigeriahealthwatch.com/wp-content/uploads/2021/03/Hand-Pump-Borehole-in-PHC-Ushafa-Bwari-Area-Council-scaled.jpg"
    )

    private fun getImageUrl(): String {
        return imageUrls[Random.nextInt(imageUrls.size)]
    }

    private val positions = listOf(
        "Governor",
        "Deputy Governor",
        "Member, House of Assembly",
        "Local Government Chairman"
    )

    private fun getPosition(): String {
        return positions[Random.nextInt(positions.size)]
    }

    private const val biography: String =
        "Duis vel nibh at velit scelerisque suscipit. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin viverra, ligula sit amet ultrices semper, ligula arcu tristique sapien, a accumsan nisi mauris ac eros. Nullam dictum felis eu pede mollis pretium. Phasellus magna."

    private val ages = listOf("50", "45", "49", "56", "60", "53", "51", "61", "64", "54")

    private fun getAge(): String {
        return ages[Random.nextInt(ages.size)]
    }

    private const val politicalAspirations =
        "Phasellus ullamcorper ipsum rutrum nunc. In ut quam vitae odio lacinia tincidunt. Nulla sit amet est."

    private const val education = "Etiam feugiat lorem non metus. Fusce a quam."

    private fun generateId(range: IntRange): String {
        return UUID.randomUUID().toString().substring(range)
    }

    private val promises = listOf(
        "Provide pipe-borne water" to "Provide free and accessible water supply for the entire people of Rivers state",
        "Good roads" to "Provide good roads within the state. Complete all ongoing road projects within Rivers state",
        "Stable power supply" to "Provide stable power supply for the entire people of Rivers state",
        "Security" to "Ensure security of lives and properties of the entire people of Rivers state",
        "Provide affordable healthcare" to "Provide free and accessible health care for the entire people of Rivers state"
    )

    private fun getRandomStatus(): String {
        val statuses = listOf(
            PromiseStatus.ABANDONED,
            PromiseStatus.COMPLETED,
            PromiseStatus.ONGOING,
            PromiseStatus.NOT_STARTED
        )
        return statuses.random().value
    }

    private fun getPromise(): Pair<String, String> {
        return promises.random()
    }

    private fun getPromises(): List<Promise> {
        val promiseList = mutableListOf<Promise>()
        repeat(promises.size) {
            val promise = getPromise()
            promiseList.add(
                Promise(
                    id = generateId(0..5),
                    promise = promise.first,
                    promiseDescription = promise.second,
                    status = getRandomStatus(),
                    promiseImages = mutableListOf(projectUrls.random())
                )
            )
        }
        return promiseList
    }

    fun getPolitician(): Politician {
        return Politician(
            id = generateId(0..17),
            fullName = getFullName(),
            party = getPoliticalParty(),
            photoUrl = getImageUrl(),
            position = getPosition(),
            age = getAge(),
            biography = biography,
            politicalAspirations = politicalAspirations,
            education = education,
            promises = getPromises()
        )
    }
}
