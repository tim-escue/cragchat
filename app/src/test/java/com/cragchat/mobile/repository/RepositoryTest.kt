package com.cragchat.mobile.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import com.cragchat.mobile.authentication.Authentication
import com.cragchat.mobile.repository.local.CragChatDatabase
import com.cragchat.mobile.repository.remote.CragChatRestApi
import com.cragchat.mobile.ui.model.Area
import com.cragchat.mobile.ui.model.pojo.PojoArea
import com.nhaarman.mockito_kotlin.whenever

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import junit.framework.Assert.*
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertThat
import org.robolectric.annotation.Config

/**
 * Created by timde on 2/22/2018.
 */
@Config(manifest= Config.NONE)
@RunWith(RobolectricTestRunner::class)
class RepositoryTest {

    @get:Rule
    var mockitoRule = MockitoJUnit.rule()!!

    @Mock
    lateinit var authentication: Authentication

    @Mock
    lateinit var database: CragChatDatabase

    @Mock
    lateinit var restApi: CragChatRestApi

    @Mock
    lateinit var area: PojoArea

    lateinit var repository: Repository

    @Before
    fun init()  {
        repository = Repository(RuntimeEnvironment.application, database, restApi, authentication)

        //Prepare restApi
        whenever(restApi.getArea(TEST_KEY, null)).thenReturn(Observable.just(area))

        //Prepare database
        whenever(database.getArea(TEST_KEY)).thenReturn(area)
    }

    @Test
    fun testGetArea() {
        repository.observeArea(TEST_KEY).subscribe(getRepositoryTestObserver(Area::class.java))
    }

    private fun <T> getRepositoryTestObserver(obj: Class<T>): Observer<T> {
        return object : Observer<T> {

            internal var count = 0

            override fun onSubscribe(d: Disposable) {

            }

            override fun  onNext(value: T) {
                validateClass(value is Any)
            }

            fun <T : Any> validateClass(value: T) {
                assertThat(value, instanceOf(value::class.java))
                assertNotNull(value)
                count++
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                fail("Error for repositoryTestObserver")
            }

            override fun onComplete() {
                assertEquals(count, 2)
            }
        }
    }

    companion object {

        private val TEST_KEY = "TEST_KEY"
    }
}
