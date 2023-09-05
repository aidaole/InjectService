package com.aidaole;

import com.aidaole.injectservice.runtime.InjectManager;
import com.aidaole.lib.interfaces.DialogInterface;
import com.aidaole.lib.interfaces.WindowInterface;
import com.aidaole.injectservice.runtime.InjectService;

@InjectService(WindowInterface.class)
public class WindowInterfaceImpl implements WindowInterface {
    @Override
    public String hello() {
        DialogInterface dialog = InjectManager.get(DialogInterface.class);
        return "hello " + dialog.dialog();
    }
}
