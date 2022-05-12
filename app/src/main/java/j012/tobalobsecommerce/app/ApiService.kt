package j012.tobalobsecommerce.app

import j012.tobalobsecommerce.model.CheckOut
import j012.tobalobsecommerce.model.ResponseModel
import j012.tobalobsecommerce.model.rajaongkir.ResponseModelOngkir
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("phone") nomortlp: String,
            @Field("password") password: String,
            //@Field("fcm") fcm: String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String,
           // @Field("fcm") fcm: String
    ): Call<ResponseModel>


    @POST("cekout")
    fun checkout(
            @Body data: CheckOut
    ): Call<ResponseModel>


    @GET("produk")
    fun getProduk(): Call<ResponseModel>

    @GET("province")
    fun getProvinsi(
            @Header("key") key: String
    ): Call<ResponseModel>

    @GET("city")
    fun getKota(
            @Header("key") key: String,
            @Query("province") id:String
    ): Call<ResponseModel>

    @GET("kecamatan")
    fun getKecamatan(
            @Query("id_kota") id:Int
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
            @Header("key") key: String,
            @Field("origin") origin: String,
            @Field("destination") destination: String,
            @Field("weight") weight: Int,
            @Field("courier") courier: String
    ): Call<ResponseModelOngkir>


    @GET("cekout/user/{id}")
    fun getRiwayat(
        @Path("id") id:Int
    ): Call<ResponseModel>


    @POST("cekout/btlTrans/{id}")
    fun btlcheckout(
            @Path("id") id:Int
    ): Call<ResponseModel>
}