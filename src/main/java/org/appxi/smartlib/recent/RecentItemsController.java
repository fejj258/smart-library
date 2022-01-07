package org.appxi.smartlib.recent;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import org.appxi.javafx.control.TreeViewEx;
import org.appxi.javafx.visual.MaterialIcon;
import org.appxi.javafx.workbench.WorkbenchPane;
import org.appxi.javafx.workbench.views.WorkbenchSideViewController;
import org.appxi.prefs.UserPrefs;
import org.appxi.smartlib.AppContext;
import org.appxi.smartlib.item.Item;
import org.appxi.smartlib.item.ItemEvent;
import org.appxi.smartlib.item.ItemProviders;
import org.appxi.timeago.TimeAgo;
import org.appxi.util.NumberHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RecentItemsController extends WorkbenchSideViewController {
    private TreeViewEx<Object> treeView;

    public RecentItemsController(WorkbenchPane workbench) {
        super("RECENT", workbench);
        this.setTitles("最近");
        this.viewGraphic.set(MaterialIcon.HISTORY.graphic());
    }

    @Override
    public void initialize() {
        app.eventBus.addEventHandler(ItemEvent.VISITED, event -> UserPrefs.recents.setProperty(
                event.item.getPath().concat(".v"),
                String.valueOf(event.item.provider.providerId())
                        .concat("|").concat(String.valueOf(System.currentTimeMillis()))
                        .concat("|").concat(event.item.getName()))
        );
    }

    @Override
    protected void onViewportInitOnce() {
    }

    @Override
    public void onViewportShowing(boolean firstTime) {
        if (firstTime) {
            this.treeView = new TreeViewEx<>();
            this.treeView.setRoot(new TreeItem<>("ROOT"));
            this.treeView.setEnterOrDoubleClickAction((e, treeItem) -> {
                if (treeItem.getValue() instanceof Item rItem && null != rItem.provider)
                    app.eventBus.fireEvent(new ItemEvent(ItemEvent.VIEWING, rItem));
            });
            this.treeView.setCellFactory(v -> new TreeCell<>() {
                Object updatedItem;

                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        updatedItem = null;
                        this.setText(null);
                        this.setGraphic(null);
                        return;
                    }
                    if (item == updatedItem)
                        return;//
                    updatedItem = item;
                    String text;
                    if (item instanceof String str) {
                        text = str;
                        this.setGraphic(getTreeItem().isExpanded() ? MaterialIcon.FOLDER_OPEN.graphic() : MaterialIcon.FOLDER.graphic());
                    } else if (item instanceof Item rItem) {
                        text = rItem.getName();
                        this.setGraphic(null);
                    } else {
                        text = item.toString();
                        this.setGraphic(null);
                    }
                    this.setText(text);
                }
            });
            this.viewport.setCenter(this.treeView);
        }

        final TreeItem<Object> treeRoot = this.treeView.getRoot();
        final List<Item> recents = new ArrayList<>(128);
        UserPrefs.recents.getPropertyKeys().forEach(key -> {
            if (!key.endsWith(".v"))
                return;
            final String val = UserPrefs.recents.getString(key, null);
            if (null == val) return;
            final String[] arr = val.split("\\|", 3);
            if (arr.length != 3) return;

            final String pid = "null".equals(arr[0]) ? null : arr[0];
            final Item item = new Item(arr[2], key.substring(0, key.length() - 2),
                    ItemProviders.find(p -> !p.isDirectory() && Objects.equals(p.providerId(), pid)));
            item.attr(Date.class, NumberHelper.toLong(arr[1], 0));
            recents.add(item);
        });
        if (recents.isEmpty()) {
            treeRoot.getChildren().setAll(new TreeItem<>("似乎还没有阅读过"));
        } else {
            recents.sort((v1, v2) -> ((Long) v2.attr(Date.class)).compareTo(v1.attr(Date.class)));
            TreeItem<Object> group = null;
            final List<TreeItem<Object>> groups = new ArrayList<>();
            for (Item recent : recents) {
                final String timeAgo = TimeAgo.using(recent.attr(Date.class), AppContext.timeAgoI18N());
                if (null == group || !Objects.equals(group.getValue(), timeAgo)) {
                    groups.add(group = new TreeItem<>(timeAgo));
                }
                group.getChildren().add(new TreeItem<>(recent));
            }
            treeRoot.getChildren().setAll(groups);
            treeRoot.getChildren().get(0).setExpanded(true);
        }
    }

    @Override
    public void onViewportHiding() {
    }
}
