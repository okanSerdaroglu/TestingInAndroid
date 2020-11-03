package com.okan.market

import android.content.Context

class ResourceCompare {

	fun isEqual(context: Context, resID: Int, string: String): Boolean {
		return context.getString(resID) == string
	}

}