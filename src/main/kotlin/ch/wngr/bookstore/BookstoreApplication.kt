package ch.wngr.bookstore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookstoreApplication {
//    companion object {
//        init {
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
//        }
//    }
}

fun main(args: Array<String>) {
    runApplication<BookstoreApplication>(*args)
}
