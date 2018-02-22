public class ArticlesListFragment extends BaseArticlesGridViewFragment<Article> {
    @Override
    protected void resizeConvertView(int position, View convertView, Article targetVo, String skipType) {
        if (!targetVo.getViewtype().equals(skipType) && !targetVo.getViewtype().equals(ViewTypes.Magazine.name())) {
            GridView.LayoutParams result = new GridView.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (targetVo != null && targetVo.getWeight() > 1) {
                double expand = targetVo.getWeight();
                result.width = (int) ((Utils.getWindowSize().x / MaxGridColumns) * expand);
            } else if (targetVo != null && targetVo.getWeight() == 1) {
                result.width = (int) ((int) (Utils.getWindowSize().x / MaxGridColumns));
            } else {
                result.width = 0;
            }
            result.height = (int) ((Utils.getWindowSize().x / (MaxGridColumns / 2) / 5) * 6);
            convertView.setLayoutParams(result);
        } else if (targetVo.getViewtype().equals(ViewTypes.Magazine.name())) {
            GridView.LayoutParams result = new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            result.width = (int) ((Utils.getWindowSize().x / MaxGridColumns) * targetVo.getWeight());
            result.height = (int) (Utils.getWindowSize().x / (MaxGridColumns / 2) / 4);
            convertView.setLayoutParams(result);
        }
    }

    @SuppressLint("NewApi")
    private View updateConvertViewBaseGridViewLocal(final int position,
                                                    ViewGroup parent, View convertView, final Article targetVo) {
        if (isAsymmetricGrid())
            resizeConvertView(position, convertView, targetVo, ViewTypes.headerView.name());
        SetView(position, convertView, R.id.ll_imageFrame, new IViewSetter<FrameLayout>() {

            @Override
            public boolean clearView(FrameLayout target) {
                switch (ViewTypes.getById(getItemViewType(position))) {
                    case Default:
                        if (isAsymmetricGrid()) {
                            target.getLayoutParams().height = (int) ((Utils.getWindowSize().x / (MaxGridColumns / 2)) * 3 / 4);
                        } else {
                            if (Utils.isDevicePhone()) {
                                Integer imageMaxWidth = (int) (Utils.getWindowSize().x * 0.33);
                                target.getLayoutParams().height = (int) (imageMaxWidth * 3 / 4);
                                target.getLayoutParams().width = imageMaxWidth;
                            } else {
                                Integer imageMaxWidth = (int) (Utils.getWindowSize().x * 0.25);
                                target.getLayoutParams().height = (int) (imageMaxWidth * 3 / 4);
                                target.getLayoutParams().width = imageMaxWidth;
                            }
                        }
                        break;
                    case Expanded:
                        target.getLayoutParams().height = (int) ((Utils.getWindowSize().x / (MaxGridColumns / 2) / 5) * 6);
                        target.getLayoutParams().width = (int) ((Utils.getWindowSize().x + 5 * Utils.getDeviceDensity()) / 2);
                        break;
                    case headerView:
                        if (!isAsymmetricGrid())
                            target.getLayoutParams().height = (int) (Utils.getWindowSize().y * 0.3);
                        else {
                            target.getLayoutParams().width = (int) ((Utils.getWindowSize().x / MaxGridColumns) * targetVo.getWeight());
                            target.getLayoutParams().height = (int) ((Utils.getWindowSize().x / (MaxGridColumns / 2) / 5) * 6);
                        }
                        break;
                }
                return false;
            }

            @Override
            public void setView(FrameLayout target) {

            }
        });
        SetView(position, convertView, R.id.ll_text, new IViewSetter<LinearLayout>() {

            @Override
            public boolean clearView(LinearLayout target) {
                target.setBackground(null);
                return true;
            }

            @Override
            public void setView(LinearLayout target) {
                switch (ViewTypes.getById(getItemViewType(position))) {

                    case Expanded:
                        if (isAsymmetricGrid() && targetVo.getWeight() == MaxGridColumns) {
                            int[] bgcolor = new int[]{
                                    R.color.pad_item_green_bg_bt,
                                    R.color.pad_item_pink_bg_bt,
                                    R.color.pad_item_brown_bg_bt,
                                    R.color.pad_item_blue_bg_bt};
                            target.setBackgroundColor(getResources().getColor(bgcolor[(int) (Math.random() * 4)]));
                            target.getLayoutParams().height = (int) ((Utils.getWindowSize().x / (MaxGridColumns / 2) / 5) * 6);
                            target.getLayoutParams().width = (int) ((Utils.getWindowSize().x - 10 * Utils.getDeviceDensity()) / 2);
                        }
                        break;
                }
            }
        });
        return convertView;
    }

    static public enum ViewTypes {
        Default(0, "default"), headerView(1, "headerView"), Rank(2, "rank"), Magazine(3, "magazine"), Expanded(4, "expanded");

        int id;
        String name;

        ViewTypes(int id, String name) {
            this.id = id;
            this.name = name;
        }

        static int getIdByName(String name) {
            for (ViewTypes item : ViewTypes.values()) {
                if (item.name.toLowerCase().equals(name.toLowerCase())) {
                    return item.id;
                }
            }
            return 0;
        }

        static ViewTypes getById(int id) {
            for (ViewTypes item : ViewTypes.values()) {
                if (item.id == id) {
                    return item;
                }
            }
            return null;
        }

        static ViewTypes getByName(String name) {
            for (ViewTypes item : ViewTypes.values()) {
                if (item.name == name) {
                    return item;
                }
            }
            return null;
        }
    }
