package com.sayegh.move_to_background

import android.content.Context
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


/** MoveToBackgroundPlugin  */
class MoveToBackgroundPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private var channel: MethodChannel? = null
    private var activity: ActivityPluginBinding? = null
    private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null


    private val CHANNEL_NAME: String = "move_to_background"


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        if (pluginBinding != null) {
            Log.d("tag", "Already Initialized")
        }
        pluginBinding = flutterPluginBinding
        val messenger = pluginBinding!!.binaryMessenger
        channel = MethodChannel(messenger, CHANNEL_NAME)
        channel?.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        teardownChannel()
    }

    private fun teardownChannel() {
        channel!!.setMethodCallHandler(null)
        channel = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "moveTaskToBack") {
            if (activity != null) {
                activity!!.activity.moveTaskToBack(true)
            } else {
                Log.e("MoveToBackgroundPlugin", "moveTaskToBack failed: activity=null")
            }
            result.success(true)
        } else {
            result.notImplemented()
        }
    }


    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.activity = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        this.activity = binding
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}