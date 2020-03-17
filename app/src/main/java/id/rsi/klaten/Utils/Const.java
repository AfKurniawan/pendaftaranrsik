package id.rsi.klaten.Utils;

public class Const {

    private static final String ROOT_URL = "http://103.247.9.253/booking_dokter/api/";

    // private static final String ROOT_URL = "http://192.168.43.112/booking_dokter/api/";

    //private static final String ROOT_URL_CHUSNUN = "http://192.168.43.112/booking_dokter/chusnun/";



    //private static final String ROOT_URL = "http://192.168.90.32/booking_dokter/api/";

    //public static final String LOGIN_URL = ROOT_URL + "login.php";

    public static final String CEK_EMAIL = ROOT_URL + "output.php?action=cek_email";

    public static final String LOGIN_URL = ROOT_URL + "output.php?action=login";

    public static final String REGISTER_URL = ROOT_URL + "output.php?action=insert_user";

    public static final String URL_ALL_JADWAL = ROOT_URL + "output.php?action=fetch_all_jadwal";

    public static final String URL_SINGLE_JADWAL = ROOT_URL + "search/single_jadwal2.php";

    public static final String URL_CARI_DOKTER = ROOT_URL + "search/cari_dokter.php";

    public static final String URL_CARI_DOKTER_NEW = ROOT_URL + "search/cari_dokter_new.php";

    public static final String URL_CARI_DOKTER_ALL_JADWAL = ROOT_URL + "search/cari_dokter_all_jadwal.php";

    public static final String URL_CARI_DOKTER_ALL_JADWAL_NEW = ROOT_URL + "search/cari_dokter_all_jadwal_new.php";

    public static final String  URL_GET_JADWAL_BY_DAY = ROOT_URL + "search/getJadwalByDay.php";

    public static final String  URL_GET_JADWAL_EKSEKUTIF = ROOT_URL + "search/getJadwalEksekutifByDay.php";

    public static final String  LIMIT_DOKTER_URL = ROOT_URL + "search/limit_dokter_new.php";

    public static final String  URL_CEK_VERSION = ROOT_URL + "search/cek_version.php";

    public static final String URL_HISTORY = ROOT_URL + "search/get_history_booking_new.php";

    public static final String URL_USER_DETAIL = ROOT_URL + "search/get_user_detail.php";

    public static final String URL_UPLOAD_BPJS = ROOT_URL + "upload_bpjs.php";

    public static final String URL_UPLOAD_KARTU = ROOT_URL + "upload_kartu.php";

    public static final String URL_UPLOAD_KTP = ROOT_URL + "upload_ktp.php";

    public static final String URL_CEK_ANTRI =  ROOT_URL + "antrian.php";

    public static final String URL_ANTRIAN_CHUSNUN =  ROOT_URL + "search/getantrianchusnun.php";

    public static final String URL_SEARCH_PASIEN = ROOT_URL + "search/cari_pasien.php";

    public static final String URL_GET_TGL_KONTROL = ROOT_URL + "search/get_tanggal_kontrol.php";

    //public static final String URL_SEARCH_PASIEN = ROOT_URL + "http://103.247.9.253/booking_dokter/api/search/cari_pasien.php";

    //public static final String BOOKING_URL = ROOT_URL + "output.php?action=insert_booking";

    public static final String BOOKING_URL = ROOT_URL + "search/insert_booking_new.php";

    public static final String GANTI_PASSWORD_URL = ROOT_URL + "search/ganti_password.php";

    public static final String CEK_BOOKING_URL = ROOT_URL + "search/limit_booking.php";

    public static final String CEK_TGL_KONTROL_URL = ROOT_URL + "search/cek_tanggal_kontrol.php";

    public static final String UPDATE_STATUS_URL = ROOT_URL + "search/delete_history_booking.php";

    public static String URL_REGISTER_TOKEN = "http://103.247.9.253/booking_dokter/notifikasi/RegisterDevice.php";

    public static String URL_ANTRIAN_WEBVIEW = "http://103.247.9.253/antri/";

    //public static String URL_ANTRIAN_WEBVIEW = "http://192.168.43.239:3000";





    public static final String IMAGE_DIRECTORY_NAME = "Upload";

    public static final int REQCODE = 100;

    public static final int REQCODECARD = 101;

    public static final int REQCODEKTP = 102;

    public static final int REQCODEGALLERYBPJS = 103;

    public static final int REQCODEGALLERYKONTROL = 104;

    public static final String image = "image";

    public static final String imageName = "foto_bpjs";

    public static final String ktpName = "ktp";

    public static final String imageKtp = "foto_ktp";

    public static final String imageCard = "kartu";

    public static final String cardName = "nama_kartu";

    public final static int QRcodeWidth = 500 ;

    private static final String IMAGE_DIRECTORY = "/Rsik";

    //FIREBASE

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static final String CHANNEL_ID = "my_channel_01";


}
