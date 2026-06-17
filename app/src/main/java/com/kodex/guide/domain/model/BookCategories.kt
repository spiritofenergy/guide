package com.kodex.guide.domain.model

enum class BookCategories(
    val id : Int
)  {
     ANIMALS (0),
     PLANTS  (1),
     WORK (2),
     SERVICES (3),
     REAL_ESTATE (4),
     AUTO (5),
     ELECTRONICS (6),
     ENTERTAINMENTS (7),
     MISCELLANEOUS (8),
     ALL (9),
     FAVORITES (10);

    companion object{
        fun fromId(id: Int): BookCategories{
            return entries.firstOrNull{
                entry ->
                entry.id == id
            } ?: ALL
        }
    }
 }