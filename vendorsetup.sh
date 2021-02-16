VT=vendor/realme/RMX3031/RMX3031-vendor.mk
if ! [ -a $VT ]; then git clone https://github.com/nishant6342/vendor_realme_RMX3031 -b S vendor/realme/RMX3031
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
