package com.bsoft.deploy.service;

import com.bsoft.deploy.context.Constant;
import com.bsoft.deploy.context.Global;
import com.bsoft.deploy.dao.entity.App;
import com.bsoft.deploy.dao.entity.AppPackage;
import com.bsoft.deploy.dao.entity.Guard;
import com.bsoft.deploy.dao.entity.Order;
import com.bsoft.deploy.dao.mapper.AppMapper;
import com.bsoft.deploy.file.FileWalker;
import com.bsoft.deploy.send.CmdSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * desc
 * Created on 2018/8/23.
 *
 * @author yangl
 */
@Service
public class AppService {
    @Autowired
    AppMapper appMapper;

    public List<App> loadApps() {
        return appMapper.loadApps();
    }


    public void saveApp(App app) {
        appMapper.save(app);
    }

    public void updateApp(App app) {
        appMapper.update(app);
    }

    public void deleteApp(int appId) {
        appMapper.delete(appId);
    }

    public List<AppPackage> loadAppPackages(int appId) {
        return appMapper.loadAppPackages(appId);
    }

    public AppPackage findAppPackageById(int pkgId) {
        return appMapper.findAppPackageById(pkgId);
    }

    public void saveAppPackage(AppPackage appPackage) {
        appMapper.saveAppPackage(appPackage);
    }

    public void updateAppPackage(AppPackage appPackage) {
        appMapper.updateAppPackage(appPackage);
    }

    /**
     * 查询是否有更新
     *
     * @param appId
     * @param pkgId
     */
    public boolean hasUpdateVersion(int appId, int pkgId) {
        return appMapper.hasUpdateVersion(appId, pkgId) > 0;
    }

    /**
     * 获取更新包,不包含传入的pkgId
     *
     * @param appId
     * @param pkgId
     * @return
     */
    public List<AppPackage> getUpdates(int appId, int pkgId) {
        return appMapper.getUpdates(appId, pkgId);
    }

    /**
     * 节点更新指点更新包
     *
     * @param slaveAppId
     * @param pkgId
     */
    public Map<String, Object> slaveAppUpdate(int slaveAppId, int pkgId) {
        FileWalker fw = Global.getAppContext().getBean(FileWalker.class);
        // 判断节点是否执行更新
        if (!fw.isRunning(slaveAppId)) {
            return fw.updateToSlave(slaveAppId, pkgId);
        } else {
            Guard guard = fw.getGuard(slaveAppId);
            Map<String, Object> res = new HashMap<>();
            res.put("guard", guard);
            res.put("code", 9);
            return res;
        }
    }

    public void reloadCache(int appId) {
        Order order = new Order();
        order.setType(Constant.CMD_RELOAD_CACHE);
        Map<String,Object> req = new HashMap<>();
        req.put("target","app");
        // 0 表示全部
        req.put("id",appId);
        order.setReqData(req);
        CmdSender.handOut(order);
    }

    public void reloadCache() {
        Global.getAppStore().reloadAll();
        reloadCache(0);
    }

    public Guard slaveAppGuard(int slaveAppId) {
        FileWalker fw = Global.getAppContext().getBean(FileWalker.class);
        return fw.getGuard(slaveAppId);
    }

    public void slaveAppUpdateSuccess(int slaveAppId) {
        FileWalker fw = Global.getAppContext().getBean(FileWalker.class);
        fw.success(slaveAppId);
    }

    public void slaveAppUpdateFail(int slaveAppId) {
        FileWalker fw = Global.getAppContext().getBean(FileWalker.class);
        fw.fail(slaveAppId);
    }
}
