/*
 * Copyright (C) 2021 The LineageOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <android-base/logging.h>
#include <libinit_utils.h>
#include "vendor_init.h"

void vendor_load_properties() {
    if (access("/system/bin/recovery", F_OK) == 0) {
        property_override("ro.adb.secure", "0");
        property_override("ro.debuggable", "1");
        property_override("ro.adb.secure.recovery", "0");
        property_override("persist.sys.usb.config", "adb");
    }
}
