echo start cloning repos
VT=vendor/realme/RMX3031/RMX3031-vendor.mk
if ! [ -a $VT ]; then git clone https://gitlab.com/nishant6342/vendor_realme_rmx3031 -b RMUI4-OSS vendor/realme/RMX3031
fi
KT=kernel/realme/mt6893/Makefile
if ! [ -a $KT ]; then git clone --depth=1 https://github.com/nishant6342/kernel_realme_RMX3031 -b S kernel/realme/mt6893
fi
PA=packages/apps/prebuilt-apps/prebuilt-apps.mk
if ! [ -a $PA ]; then git clone --depth=1 https://gitlab.com/nishant6342/packages_apps_prebuilt-apps packages/apps/prebuilt-apps/
fi
MTK_SEPOLICY=device/mediatek/sepolicy_vndr/SEPolicy.mk
if ! [ -a $MTK_SEPOLICY ]; then git clone https://github.com/Project-Elixir/device_mediatek_sepolicy_vndr device/mediatek/sepolicy_vndr
fi
PARTS=packages/apps/RealmeParts/parts.mk
if ! [ -a $PARTS ]; then git clone https://github.com/nishant6342/packages_apps_RealmeParts packages/apps/RealmeParts
fi
POCKET=packages/apps/PocketMode/pocket_mode.mk
if ! [ -a $POCKET ]; then git clone https://github.com/nishant6342/packages_apps_PocketMode packages/apps/PocketMode
fi
FW=vendor/realme/RMX3031-firmware/Android.mk
if ! [ -a $FW ]; then git clone https://github.com/nishant6342/vendor_realme_RMX3031-firmware vendor/realme/RMX3031-firmware
fi
COMPACT=hardware/lineage/compat/Android.bp
if ! [ -a $COMPACT ]; then git clone https://github.com/LineageOS/android_hardware_lineage_compat -b lineage-20.0 hardware/lineage/compat
fi
MTK=hardware/mediatek/Android.bp
if ! [ -a $MTK ]; then git clone https://github.com/LineageOS/android_hardware_mediatek -b lineage-20 hardware/mediatek
fi
echo end cloning
echo start cherry-picking required commit in FWB
cd frameworks/base && git fetch https://github.com/Project-Elixir/android_frameworks_base 34e724ebc06723e82756a3473ad940f6b22f217d && git cherry-pick FETCH_HEAD; git cherry-pick --abort >/dev/null 2>&1; cd ../..
echo Done cherry-pick, Returned to root directory of ROM
