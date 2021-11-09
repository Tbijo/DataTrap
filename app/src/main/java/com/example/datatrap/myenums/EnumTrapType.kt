package com.example.datatrap.myenums

enum class EnumTrapType(val myName: String) {
    LIVE_TRAPS("Live Traps"),
    SNAP_TRAPS("Snap Traps");

    companion object {
        fun myValues(): List<String> {
            val list = mutableListOf<String>()
            values().forEach {
                list.add(it.myName)
            }
            return list
        }
    }
}