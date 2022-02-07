package com.example.library.cache

import android.graphics.Bitmap
import androidx.room.*

@Entity(tableName = "cache")
class Cache {
    @PrimaryKey(autoGenerate = false)
    var key: String = ""

    var data: ByteArray? = null

//    @ColumnInfo(name = "columnId")//  通过@ColumnInfo注解为字段指定别名
//    var column: Int = 0

//    @Ignore  //通过  @Ignore注解忽略该字段对表的映射
//    var bitmap: Bitmap? = null

//    @Embedded  //如果想让你的内嵌对象的字段也映射到数据库中的字段  拥有一个主键
//    var user: User? = null

}

//@Entity(tableName = "user")
//class User {
//}
