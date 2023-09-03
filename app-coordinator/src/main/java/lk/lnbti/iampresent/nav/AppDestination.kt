package lk.lnbti.iampresent.nav

interface AppDestination {
    val route: String
}

object LectureList : AppDestination{
    override val route="lecture_list"
}