package com.architect.banking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Architect Banking app.
 *
 * [HiltAndroidApp] triggers Hilt's code generation and initialises the
 * application-level dependency graph. All [SingletonComponent] bindings
 * are created here.
 */
@HiltAndroidApp
class SynchBankApplication : Application()
