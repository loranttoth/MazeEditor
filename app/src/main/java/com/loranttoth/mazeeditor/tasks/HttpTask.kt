package com.loranttoth.mazeeditor.tasks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class HttpTask (var context: Context) {
    fun login() {
        val url = ""

        // Post parameters
        // Form fields and values
        val params = HashMap<String,String>()
        params["grant_type"] = "password"
        params["client_id"] = ""
        params["client_secret"] = ""
        params["username"] = ""
        params["password"] = ""
        params["scope"] = "basic email"
        val jsonObject = JSONObject(params as Map<*, *>?)

        // Volley post request with parameters
        val request = JsonObjectRequest(
            Request.Method.POST,url,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    println(response.toString())
                }catch (e:Exception){
                    println("Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
                println("Volley error: $it")
            })


        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // 0 means no retry
            0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
            1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }
}