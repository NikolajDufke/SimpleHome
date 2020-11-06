package com.example.simplehome.HomeAssistantConnection

import android.util.Log
import com.example.simplehome.Repository.Entities
import com.example.simplehome.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.sql.Timestamp




class SocketManager(baseUri: Any?) {




    companion object{
        private val TAG = "websocket"
        private val baseurl = "192.168.178.2:8123"
        private val baseUri: URI? = URI("ws://" + baseurl + "/api/websocket")
        private lateinit var ws : WebSocketClient
        var isAuthenticationAproved  = false

        var _messageIs = 0
        var messageId :Int = 0
            get() {
                _messageIs++
                return _messageIs
            }

        fun Start()  {

            CoroutineScope(IO).launch {
                run {
                    Log.d("SocketManager", "Connectiong to websocket")
                    ws = webSocket(baseUri)
                    ws.connect()
                }
            }
        }

        fun sendMessage(message: String){
            Log.d(TAG, "sending message " + message)
            ws.send(message)
        }
    }


    class webSocket(serverUri: URI?) : WebSocketClient(serverUri){


        private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjZDRiNzQwM2E2ZmM0NGVmOTU5NTMyYTkzMjJlMTNkOCIsImlhdCI6MTYwMzkxNDY1OCwiZXhwIjoxOTE5Mjc0NjU4fQ.6UYA1Px1_zqTK-bBpX-3gzm9F_p5Y2Q6CYcLaBUVpRE"
        private val TAG = "SocketManager"
        private val awatingResults  = mutableMapOf<Int,String>()

        override fun onOpen(handshakedata: ServerHandshake?) {

        Log.d(TAG, "webSocket open: " + Timestamp(System.currentTimeMillis()))

    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d(TAG, "webSocket closed: "+ Timestamp(System.currentTimeMillis()))
        //TODO("Not yet implemented")
    }

    override fun onMessage(message: String?) {
        //TODO("Not yet implemented")
        Log.d(TAG, "webSocket message: " + message)

        val resultinfo = Gson().fromJson(message, Resultinfo::class.java)

        val type = resultinfo.type
        when (type){
            "auth_required" -> {

                HomeAssistActions().sendMessage<AuthenticationCall>(AuthenticationCall(token, "auth"))
            }
            "auth_ok" -> {
                isAuthenticationAproved = true


                HomeAssistActions().sendMessage<baseCall>(
                    baseCall(
                        messageId,
                        "subscribe_events"
                    )
                )
                Log("auth_ok", "subscribe_events")

               val id = messageId
                awatingResults.put(id, "get_states")
                HomeAssistActions().sendMessage<baseCall>(
                    baseCall(
                        id,
                        "get_states")
                )
                Log("auth_ok", "get_states")

            }
            "result" -> {
               Log( "result", message)
               val gsonFromResponse =  GsonBuilder()
                    .registerTypeAdapter(baseResponse::class.java, ResponseDeserializer())
                    .create()

                val res = gsonFromResponse.fromJson(message,
                    baseResponse::class.java )

                Entities.onEntityGetAll(res.result)
            }
            "event" -> {
                Log("event", message)
                val gsonFromEvent = GsonBuilder()
                    .registerTypeAdapter(baseEvent::class.java, EventDeserializer())
                    .create()

                val event = gsonFromEvent.fromJson(message, baseEvent::class.java)

                event.event?.data?.new_state?.let { Entities.onEntityUpdate(it) }
            }
        }
    }

    override fun onError(ex: Exception?) {
        //TODO("Not yet implemented")
        Log.d(TAG, "webSocket error: " + ex)
    }


        fun Log(where : String , message : String?){
            Log.d(TAG, "webSocket " + where + ": " + message)
        }
    }
}






