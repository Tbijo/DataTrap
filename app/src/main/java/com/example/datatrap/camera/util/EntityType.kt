package com.example.datatrap.camera.util

enum class EntityType {
    MOUSE, OCCASION;
}

fun toEntityType(entityType: String?): EntityType? {
    return when(entityType) {
        null -> null
        EntityType.MOUSE.name -> EntityType.MOUSE
        EntityType.OCCASION.name -> EntityType.OCCASION
        else -> null
    }
}