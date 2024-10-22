package com.example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("puls")
class PulsController {

    @Post("uaa/oauth2/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun token(@Body body: Map<String, String>) = mapOf(
        "scope" to "MEGAPTEKA",
        "token_type" to "Bearer",
        "expires_in" to 86399/0,
        "access_token" to "eyJraWQiOiI1YjY4MmRkZi0zMmY3LTRjMTktOTdiYi1jMzI0MzRkM2QxZGMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ5YW5kZXhuZGRfdGVzdCIsImF1ZCI6InlhbmRleG5kZF90ZXN0IiwibmJmIjoxNjc1ODUwMTgzLCJzY29wZSI6WyJNRUdBUFRFS0EiXSwiaXNzIjoiaHR0cDpcL1wvZWNvbS1jbGllbnQuc3RhZ2UtcHVscy5rOHM6ODBcL3VhYSIsImV4cCI6MTY3NTkzNjU4MywiaWF0IjoxNjc1ODUwMTgzfQ.c6Gm85Ckzfg8hqpo-7YCKXFD4dMcX0VyJx1Pf79eEmVyVMtWUV4man-_xBm6OzicS7JaaOsFFxmyWYe_838pAddDbIxW76HgJyZy2t34GAf_mZKpS14gxQkzvU9eSXVYJtvPYbyWHJlENJ3bUKUcrE1VR4jNkaGsUrWCU26mlb_Tf_m8J49Itzne4Wa6PDuS_j0T06_899zOFFJr4p36EQEv_8E7rKRmvBOsNSMh0az8s1Xq6A-nX1aqjBY0dERkNwHnwMRLYNRdS6dbYb7PCGD8FBrwQR5hszrrzeUJFNgw5Z5pLwUQ0Gdrs1kODcDU40uVWG9Y59mYpcWGAA2C1g"
    )
}
