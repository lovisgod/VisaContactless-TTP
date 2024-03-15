package com.lovisgod.testVisaTTP.handlers.network

import java.util.Date

var date = Date()

data class CashoutRequestBody (

    var merchantCategoryCode : String?  = null,
    var terminalCode         : String?  = null,
    var merchantName         : String?  = null,
    var merchantId           : String?  = null,
    var haspin               : Boolean? = null,
    var track2Data           : String?  = null,
    var panSequenceNumber    : String?  = null,
    var amount               : String?  = null,
    var pinBlock             : String?  = null,
    var posDataCode          : String?  = null,
    var iccString            : String?  = null,
    var agentPhoneNumber     : String? = "0000000000",
    var userPhoneNumber      : String? = "08165656988",
    var field7               : String? = DateUtils.timeAndDateFormatter.format(date),
    var field12              : String? = DateUtils.timeFormatter.format(date),
    var agentTransType       : String? = "push"

)

data class GenericTmsBody (
   var email: String? = ""
 )