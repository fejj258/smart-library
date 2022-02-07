package org.appxi.smartlib.recent;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import org.appxi.holder.RawHolder;
import org.appxi.javafx.app.AppEvent;
import org.appxi.javafx.app.DesktopApp;
import org.appxi.javafx.helper.FxHelper;
import org.appxi.javafx.visual.MaterialIcon;
import org.appxi.javafx.workbench.WorkbenchPane;
import org.appxi.javafx.workbench.WorkbenchViewController;
import org.appxi.javafx.workbench.views.WorkbenchMainViewController;
import org.appxi.prefs.Preferences;
import org.appxi.prefs.PreferencesInProperties;
import org.appxi.prefs.UserPrefs;
import org.appxi.smartlib.dao.DataApi;
import org.appxi.smartlib.html.HtmlViewer;
import org.appxi.smartlib.item.FileProvider;
import org.appxi.smartlib.item.Item;
import org.appxi.smartlib.item.ItemProviders;
import org.appxi.smartlib.item.ItemViewer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecentViewsController extends WorkbenchViewController {
    public RecentViewsController(WorkbenchPane workbench) {
        super("recentViews", workbench);
        this.setTitles("RecentViews");
        this.graphic.set(MaterialIcon.HISTORY.graphic());
    }

    @Override
    public <T> T getViewport() {
        return null;
    }

    @Override
    public void initialize() {
        app.eventBus.addEventHandler(AppEvent.STOPPING, event -> saveRecentViews());
        app.getPrimaryScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F1), this::showWelcome);
        //
        final RawHolder<WorkbenchMainViewController> swapRecentViewSelected = new RawHolder<>();
        final List<WorkbenchMainViewController> swapRecentViews = new ArrayList<>();
        app.eventBus.addEventHandler(AppEvent.STARTING, event -> {
            final Preferences recents = createRecentViews(true);
            WorkbenchMainViewController addedController = null;

            for (String key : recents.getPropertyKeys()) {
                final String[] arr = recents.getString(key, "").split("\\|", 3);
                if (arr.length != 3) continue;

                if (!DataApi.dataAccess().exists(key))
                    continue;

                final String pid = "null".equals(arr[0]) ? null : arr[0];
                final Item item = new Item(arr[2], key,
                        ItemProviders.find(p -> !p.isDirectory() && Objects.equals(p.providerId(), pid)));

                if (null != item.provider.getViewer()) {
                    addedController = item.provider.getViewer().apply(item);
                    if (null == addedController) continue;
                    if ("true".equals(arr[1]))
                        swapRecentViewSelected.value = addedController;
                    swapRecentViews.add(addedController);
                }
            }
            if (!swapRecentViews.isEmpty()) {
                FxHelper.runLater(() -> {
                    for (WorkbenchMainViewController viewController : swapRecentViews) {
                        workbench.addWorkbenchViewAsMainView(viewController, true);
                    }
                });
                if (null == swapRecentViewSelected.value)
                    swapRecentViewSelected.value = addedController;
            }
        });
        app.eventBus.addEventHandler(AppEvent.STARTED, event -> FxHelper.runThread(100, () -> {
            if (swapRecentViews.isEmpty()) {
                showWelcome();
            } else {
                swapRecentViews.forEach(WorkbenchViewController::initialize);
                if (null != swapRecentViewSelected.value)
                    workbench.selectMainView(swapRecentViewSelected.value.id.get());
            }
        }));
    }

    private Preferences createRecentViews(boolean load) {
        return new PreferencesInProperties(UserPrefs.confDir().resolve(".recentviews"), load);
    }

    private void saveRecentViews() {
        final Preferences recents = createRecentViews(false);
        workbench.mainViews.getTabs().forEach(tab -> {
            if (tab.getUserData() instanceof ItemViewer itemView) {
                recents.setProperty(itemView.item.getPath(),
                        String.valueOf(itemView.item.provider.providerId())
                                .concat("|").concat(String.valueOf(tab.isSelected()))
                                .concat("|").concat(itemView.item.getName())
                );
            }
        });
        recents.save();
    }

    @Override
    public void onViewportShowing(boolean firstTime) {
    }

    private void showWelcome() {
        Path indexHtml = DesktopApp.appDir().resolve("template/index.html");
        Item indexItem = new Item("欢迎使用", indexHtml.toString(), FileProvider.ONE);
        HtmlViewer newViewer = new HtmlViewer(indexItem, workbench);
        // 优先查找存在的视图，以避免重复打开
        HtmlViewer oldViewer = (HtmlViewer) workbench.findMainViewController(newViewer.id.get());

        FxHelper.runLater(() -> {
            if (null != oldViewer) {
                workbench.selectMainView(oldViewer.id.get());
                return;
            }

            workbench.addWorkbenchViewAsMainView(newViewer, false);
            newViewer.initialize();
            workbench.selectMainView(newViewer.id.get());
        });
    }
}
