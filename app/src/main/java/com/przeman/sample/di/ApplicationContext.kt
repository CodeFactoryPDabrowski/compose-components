package com.przeman.sample.di

import android.content.Context
import javax.inject.Qualifier

/** Qualifier to denote a [Context] that is specifically an Application context. */
@Qualifier
annotation class ApplicationContext
