package com.ruoyi.common.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class TreeselectUtils {

    /**
     * 将扁平列表转换为树形结构
     *
     * @param list           扁平数据列表
     * @param idGetter       用于获取节点 ID 的函数（例如：Node::getId）
     * @param parentIdGetter 用于获取父节点 ID 的函数（例如：Node::getParentId）
     * @param childrenSetter 用于设置子节点集合的函数（例如：Node::setChildren）
     * @param <T>            节点类型
     * @param <ID>           ID 类型（如 Long、String 等）
     * @return 树形结构的根节点列表
     */
    public static <T, ID> List<T> build(List<T> list,
                                        Function<T, ID> idGetter,
                                        Function<T, ID> parentIdGetter,
                                        BiConsumer<T, List<T>> childrenSetter) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Map<ID, List<T>> childrenMap = new HashMap<>(list.size()); // 初始容量设为list大小，减少扩容
        Map<ID, T> nodeMap = new HashMap<>(list.size());
        List<T> roots = new ArrayList<>();

        // 一次遍历完成：构建childrenMap、nodeMap、收集根节点
        for (T item : list) {
            ID id = idGetter.apply(item);
            ID parentId = parentIdGetter.apply(item);

            // 构建子节点映射（简化逻辑，减少containsKey判断）
            childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(item);
            // 构建节点ID映射
            nodeMap.put(id, item);
            // 判断是否为根节点（父ID不存在于节点映射中）
            if (!nodeMap.containsKey(parentId)) { // 此时nodeMap尚未包含当前item，若parentId是当前id则会误判？
                roots.add(item);
            }
        }

        // 处理特殊情况：若根节点的parentId等于自身ID（如parentId=0且自身ID=0），需重新过滤
        // 注：根据业务场景决定是否保留，若业务中根节点parentId为null或特定值，可简化
        roots.removeIf(root -> nodeMap.containsKey(parentIdGetter.apply(root)));

        // 为根节点设置子节点（迭代方式）
        roots.forEach(root -> adaptChildren(root, childrenMap, idGetter, childrenSetter));

        return roots;
    }

    /**
     * 迭代方式构建子节点（替代递归，避免栈溢出）
     */
    private static <T, ID> void adaptChildren(T node,
                                              Map<ID, List<T>> childrenMap,
                                              Function<T, ID> idGetter,
                                              BiConsumer<T, List<T>> childrenSetter) {
        Deque<T> stack = new ArrayDeque<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            T current = stack.pop();
            ID currentId = idGetter.apply(current);
            List<T> children = childrenMap.get(currentId);

            if (CollectionUtils.isNotEmpty(children)) {
                childrenSetter.accept(current, children);
                // 子节点入栈，继续处理
                children.forEach(stack::push);
            }
        }
    }
}
