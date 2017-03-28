package me.junbin.gradprj.util;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import me.junbin.commons.util.CollectionUtils;
import me.junbin.gradprj.domain.Perm;
import me.junbin.gradprj.domain.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/26 19:29
 * @description :
 */
public abstract class ZTreeUtils {

    private static final List<JsonObject> emptyTree = Collections.emptyList();

    public static List<JsonObject> constructCheckedTree(Collection<Perm> relations, Collection<String> checkedList) {
        if (CollectionUtils.isEmpty(relations)) {
            return emptyTree;
        }
        Collection<String> checked = new HashSet<>();
        if (CollectionUtils.notEmpty(checkedList)) {
            checked.addAll(checkedList);
        }
        List<JsonObject> tree = new ArrayList<>(relations.size());
        for (Perm perm : relations) {
            if (checked.contains(perm.getId())) {
                tree.add(asCheckNode(perm, true));
            } else {
                tree.add(asCheckNode(perm, false));
            }
        }
        return tree;
    }

    private static JsonObject asCheckNode(Perm perm, boolean checked) {
        JsonObject node = new JsonObject();
        node.addProperty("id", perm.getId());
        node.addProperty("name", perm.getPermName());
        node.addProperty("checked", checked);
        String pid = perm.getParentId();
        if (StringUtils.isEmpty(pid)) {
            node.add("parentId", JsonNull.INSTANCE);
            node.addProperty("isParent", true);
        } else {
            node.addProperty("parentId", pid);
        }
        return node;
    }

    public static List<JsonObject> constructCheckedRoleTree(Collection<Role> total, Collection<Role> checked) {
        if (CollectionUtils.isEmpty(total)) {
            return emptyTree;
        }
        Collection<Role> actualChecked = new HashSet<>();
        if (CollectionUtils.notEmpty(checked)) {
            actualChecked.addAll(checked);
        }
        return total.stream()
                    .map(role -> asCheckNode(role, actualChecked.contains(role)))
                    .collect(Collectors.toList());
    }

    private static JsonObject asCheckNode(Role role, boolean checked) {
        JsonObject node = new JsonObject();
        node.addProperty("id", role.getId());
        node.add("parentId", JsonNull.INSTANCE);
        node.addProperty("name", role.getRoleName() + '(' + role.getRoleNameCn() + ')');
        node.addProperty("checked", checked);
        return node;
    }

}
