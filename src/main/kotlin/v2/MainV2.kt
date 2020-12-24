package v2

const val DEFAULT_TEST_FILE = "D:\\TCPF-MAPPER\\tcpf-mapper\\tcpf-mapper-app\\src\\test\\resources\\ips.txt"
const val TOTAL_IPS = 4_294_967_296L // 0..TOTAL_IPS-1 or 0.0.0.0..255.255.255.255
val IP_REEXP =
    Regex("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")

//todo MAX_TASK long -> int ?

fun main() {
    //todo make uniq count

}
