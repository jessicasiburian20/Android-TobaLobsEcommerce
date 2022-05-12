package j012.tobalobsecommerce.model

class ResponseModel {
    var success = 0
    lateinit var message: String

    var user = User()
    var produks: ArrayList<Produk> = ArrayList()

    var transaksis: ArrayList<TransaksiModel> = ArrayList()

    var transaksi = TransaksiModel()

    var rajaongkir = ModelAlamat()
    var provinsi: ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten: ArrayList<ModelAlamat> = ArrayList()
    var kecamatan: ArrayList<ModelAlamat> = ArrayList()
}