/**
 * 客製化AsymmetricGridView
 * <p>
 * 1.在BaseArticlesGridViewFragment中
 * recalculateColSpan 在資料陣列放入GridView的前，事先計算需擴展寬度
 * 並塞入足夠的空白Article物件
 * <p>
 * 2.ArticlesListFragment
 * 依據計算後的item寬度，調整layout
 */
public abstract class BaseArticlesGridViewFragment<T extends Article> extends BaseGridViewFragment<T> {
    protected int MaxGridColumns = 6;//畫面寬度切格為六格
    protected int[] defaultWeight = new int[]{2, 2, 2, 3, 3, 4, 4, 6};
    //將格子寬度以defaultWeight表示(ex. 若畫面寬度為六格，則weight=2等於格子占兩格寬)

    @Override
    public MyArrayList<T> prepareAddPageItems(MyArrayList<T> articleList, String loadPhase,
                                              String loadResult) {
        if (isAsymmetricGrid()) {
            articleList = recalculateColSpan(articleList, isAsymmetricGrid());
        }
        return articleList;
    }

    protected MyArrayList<T> recalculateColSpan(MyArrayList<T> results, boolean isRandomWeight) {
        //isRandomWeight = true物件寬度由電腦隨機設定
        return recalculateColSpan(results, isRandomWeight, null);
    }

    protected MyArrayList<T> recalculateColSpan(MyArrayList<T> results, boolean isRandomWeight, String skipType) {
        MyArrayList<T> SpanedItems = new MyArrayList<T>();
        int colSpanSum = 0, preColSpanSum = 0;//colSpanSum當前寬度總和 preColSpanSum先前寬度總和
        T lastItem;
        Iterator<T> iter = results.iterator();
        while (iter.hasNext()) {
            T item = iter.next();
            //符合skipType的物件，直接設定為畫面最大寬度
            if (skipType != null && !TextUtils.isEmpty(item.getViewtype()) && item.getViewtype().equals(skipType)) {
                item.setWeight(MaxGridColumns);
                SpanedItems.add(item);
                for (int i = 0; i < MaxGridColumns - 1; i++) {
                    T empty;
                    try {
                        Class<?> clazz = item.getClass();
                        empty = (T) clazz.newInstance();
                        empty.setTitle("");
                        empty.setId(item.getId() + "random" + i);
                        empty.setWeight(0);
                        empty.setViewtype(skipType);
                        SpanedItems.add(empty);
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                continue;
            }
            if (isRandomWeight && item.getWeight() == 0)
                item.setWeight(defaultWeight[(int) (Math.random() * MaxGridColumns)]);

            preColSpanSum = colSpanSum;
            if (item.getWeight() > MaxGridColumns) {
                item.setWeight(MaxGridColumns);
            }
            colSpanSum += item.getWeight();

            if (colSpanSum <= MaxGridColumns) {//插入當前item後仍不超出每列寬度
                SpanedItems.add(item);//則直接放入SpanedItems陣列中
            } else {
                if (preColSpanSum != MaxGridColumns) {
                    //插入當前item前，若Span總數不等於最大寬度，則將最後一個SpanedItem weight增大，以便剛好填滿畫面
                    lastItem = SpanedItems.get(SpanedItems.size() - 1);
                    lastItem.setWeight(lastItem.getWeight()
                            + (MaxGridColumns - preColSpanSum));
                    SpanedItems.remove(SpanedItems.size() - 1);
                    SpanedItems.add(lastItem);
                }
                SpanedItems.add(item);
                colSpanSum = item.getWeight();
            }

        }//while end

        //檢查最後一個item寬度是否剛好貼齊螢幕右邊
        if (results != null && results.size() > 0 && colSpanSum != MaxGridColumns) {
            lastItem = SpanedItems.get(SpanedItems.size() - 1);
            SpanedItems.remove(SpanedItems.size() - 1);
            lastItem.setWeight(lastItem.getWeight() + MaxGridColumns - colSpanSum);
            SpanedItems.add(lastItem);

        }
        addEmptyItem(SpanedItems, results);
        return results;
    }

    @Override
    protected void addEmptyItem(MyArrayList<T> SpanedItems, MyArrayList<T> output) {
        output.clear();
        int randomIndex = 0;
        for (T item : SpanedItems) {
            output.add(item);
            for (int i = 0; i < item.getWeight() - 1; i++) {

                T empty;
                try {
                    Class<?> clazz = item.getClass();
                    empty = (T) clazz.newInstance();
                    empty.setTitle("");
                    empty.setId(item.getId() + "random" + randomIndex);
                    empty.setWeight(0);
                    output.add(empty);
                    randomIndex++;
                } catch (Exception ex) {
                    Log.e(TAG, "addEmptyItem failed");
                }
            }
        }
    }
}
    
