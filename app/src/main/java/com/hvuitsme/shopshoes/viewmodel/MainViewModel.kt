package com.hvuitsme.shopshoes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hvuitsme.shopshoes.model.SlideModel

class MainViewModel(): ViewModel() {
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val _banner = MutableLiveData<List<SlideModel>>()
    val banners: LiveData<List<SlideModel>> = _banner
    fun loadBanners(){
        val Ref = firebaseDb.getReference("Banner")
        Ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<SlideModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(SlideModel::class.java)
                    if (list != null){
                        lists.add(list)
                    }
                }
                _banner.postValue(lists)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}