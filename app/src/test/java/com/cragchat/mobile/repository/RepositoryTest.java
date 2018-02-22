package com.cragchat.mobile.repository;

import android.content.Context;

import com.cragchat.mobile.authentication.Authentication;
import com.cragchat.mobile.repository.local.CragChatDatabase;
import com.cragchat.mobile.repository.local.RealmDatabase;
import com.cragchat.mobile.repository.remote.CragChatRestApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by timde on 2/22/2018.
 */

public class RepositoryTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context context;

    @Mock
    Authentication authentication;

    @Mock
    CragChatDatabase database;

    @Mock
    CragChatRestApi restApi;

    @Test
  public void testGetArea() {
      Repository repository = new Repository(context, database, restApi, authentication);
      assertNotNull(repository);
  }
}
