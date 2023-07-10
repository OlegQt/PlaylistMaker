package com.playlistmaker.util

class Creator private constructor(){
    var count = 0

    fun increase(){
        this.count++
    }


    override fun toString(): String {
        return "Class Creator ($count)"
    }

    companion object{
        var instance:Creator? = null

        fun getCreator():Creator{
            if(instance==null) {
                instance = Creator()
            }
            else{
                instance?.increase()
            }
            return instance!!
        }
    }
}