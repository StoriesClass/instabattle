package me.instabattle.app.managers


import org.junit.Test

class UserManagerTest {
    @Test
    @Throws(Exception::class)
    fun getAndDo() {
        val id = 1
        //UserManager.getAndDo(id, Consumer{u-> assertEquals(1, u.id) })
    }

    @Test
    @Throws(Exception::class)
    fun getCountAndDo() {

    }

    @Test
    @Throws(Exception::class)
    fun getAllAndDo() {

    }

    @Test
    @Throws(Exception::class)
    fun updateAndDo() {

    }

    @Test
    @Throws(Exception::class)
    fun createAndDo() {
        val username = "ethan"
        val email = "h3@h3.org"
        val password = "8383"
        /*UserManager.createAndDo(username, email, password, Consumer{ u ->
            assertEquals(username, u.username)
            assertEquals(email, u.email)
        })*/
    }

}