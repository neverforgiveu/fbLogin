package com.example.fblogin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var  loginbt : LoginButton
    lateinit var   callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginbt = findViewById(R.id.login_button)
        callbackManager = CallbackManager.Factory.create();




        loginbt.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                Log.d("Success", "成功")
                getUserProfile(result?.accessToken, result?.accessToken?.userId)
            }

            override fun onCancel() {
                Log.d("Cancel", "取消")
            }


            override fun onError(exception: FacebookException) {
                Log.d("Error", "失敗")
            }
        })






    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDestroy() {
        super.onDestroy()
    }


    fun getUserPicture(token: AccessToken?){
        val request = GraphRequest.newGraphPathRequest(
            token,
            "/{user-id}/picture"
        ) {

        }

        request.executeAsync()
    }


    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "name , email , id"
        )
        GraphRequest(token,
            "/${userId}",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    tv1.text = facebookEmail.toString()
                }
                if (jsonObject.has("name")) {
                    val facebookEmail = jsonObject.getString("name")
                    name.text = facebookEmail.toString()
                }
                if (jsonObject.has("id")) {
                    val fbID = jsonObject.getString("id")
                    var bitmap: Bitmap? = null
                    try {
                        /*val imageURL =
                            "https://graph.facebook.com/" + fbID.toString() + "/picture?width=150&width=150"
                        val imageURI = URL(imageURL)
                        bitmap = BitmapFactory.decodeStream(imageURI.openConnection().getInputStream())*/
                        iv1.profileId = fbID
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }).executeAsync()
      }


}