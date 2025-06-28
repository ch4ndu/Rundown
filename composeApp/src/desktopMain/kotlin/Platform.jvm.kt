class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val target: TARGET = TARGET.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()