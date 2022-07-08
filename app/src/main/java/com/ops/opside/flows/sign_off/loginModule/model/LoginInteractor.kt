package com.ops.opside.flows.sign_off.loginModule.model

class LoginInteractor(private var email: String, private var password: String) {

    fun getEmail(): String{
        return email
    }

    fun getPassword(): String{
        return password
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun setPassword(password: String){
        this.password = password
    }

}