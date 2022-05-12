package j012.tobalobsecommerce.model.rajaongkir

import j012.tobalobsecommerce.model.ModelAlamat

class ResponseModelOngkir {
    val rajaongkir = Rajaongkir()

    class Rajaongkir{
        val results = ArrayList<Result>()
    }
}