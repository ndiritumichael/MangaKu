package com.uwaisalqadri.mangaku.data.souce.local.entity

import io.realm.RealmObject

class PosterImageObject: RealmObject {
    var large: String = ""
    var medium: String = ""
    var original: String = ""
    var small: String = ""
    var tiny: String = ""
}