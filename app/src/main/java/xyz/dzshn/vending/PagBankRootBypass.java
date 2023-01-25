package xyz.dzshn.vending;

import android.app.Activity;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PagBankRootBypass implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("br.com.uol.ps.myaccount"))
            return;

        // this app is really silly. they obfuscate their names *manually* by
        // naming things like innocent stuff. finding these methods was
        // absolute hell, but the patch is still surprisingly simple.

        var rootChecker = lpparam.classLoader.loadClass("o.InsurancePlatformSpanActionType");

        XposedBridge.hookMethod(
            rootChecker.getDeclaredMethod("readResolve", Activity.class),
            XC_MethodReplacement.returnConstant(true)
        );

        XposedBridge.hookMethod(
            rootChecker.getDeclaredMethod("readObject", Activity.class),
            XC_MethodReplacement.returnConstant(false)
        );

        XposedBridge.hookAllMethods(
            lpparam.classLoader.loadClass("o.InsurancePlatformAdviceBodyModel"),
            "readResolve",
            XC_MethodReplacement.returnConstant(null)
        );
    }
}
