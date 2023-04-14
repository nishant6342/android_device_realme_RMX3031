echo start cloning repos
VT=vendor/realme/RMX3031/RMX3031-vendor.mk
if ! [ -a $VT ]; then git clone https://github.com/nishant6342/vendor_realme_RMX3031 -b RMUI4-OSS vendor/realme/RMX3031
fi
KT=kernel/realme/RMX3031/Makefile
if ! [ -a $KT ]; then git clone --depth=1 https://github.com/nishant6342/kernel_realme_RMX3031 -b T kernel/realme/RMX3031
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
CAM=device/oplus/camera/camera.mk
if ! [ -a $CAM ]; then git clone https://gitlab.com/nishant6342/device_oplus_camera device/oplus/camera
fi
COMPACT=hardware/lineage/compat/Android.bp
if ! [ -a $COMPACT ]; then git clone https://github.com/LineageOS/android_hardware_lineage_compat -b lineage-20.0 hardware/lineage/compat
fi
MTK=hardware/mediatek/Android.bp
if ! [ -a $MTK ]; then git clone https://github.com/nishant6342/android_hardware_mediatek -b lineage-20 hardware/mediatek
fi
CLANG17=prebuilts/clang/host/linux-x86/clang-r487747/bin/clang
if ! [ -a $CLANG17 ]; then git clone https://gitlab.com/projectelixiros/android_prebuilts_clang_host_linux-x86_clang-r487747 -b Tiramisu prebuilts/clang/host/linux-x86/clang-r487747
fi
echo end cloning
