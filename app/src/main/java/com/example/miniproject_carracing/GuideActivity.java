package com.example.miniproject_carracing;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageButton btnBack;
    private GuidePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
        setupViewPager();
        setupClickListeners();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupViewPager() {
        List<GuideItem> guideItems = createGuideItems();
        adapter = new GuidePagerAdapter(guideItems);
        viewPager.setAdapter(adapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Cách Chơi");
                            tab.setIcon(R.drawable.ic_gamepad);
                            break;
                        case 1:
                            tab.setText("Thể Lệ");
                            tab.setIcon(R.drawable.ic_rules);
                            break;
                        case 2:
                            tab.setText("Mẹo Hay");
                            tab.setIcon(R.drawable.ic_tips);
                            break;
                        case 3:
                            tab.setText("Điều Khiển");
                            tab.setIcon(R.drawable.ic_controls);
                            break;
                    }
                }
        ).attach();
    }

    private List<GuideItem> createGuideItems() {
        List<GuideItem> items = new ArrayList<>();

        // Tab 1: Cách Chơi
        items.add(new GuideItem(
                "🎮 Cách Chơi Game Đua Xe",
                "Hướng dẫn chi tiết cách chơi game",
                createGameplayContent()
        ));

        // Tab 2: Thể Lệ
        items.add(new GuideItem(
                "📋 Thể Lệ và Quy Định",
                "Các quy định khi tham gia game",
                createRulesContent()
        ));

        // Tab 3: Mẹo Hay
        items.add(new GuideItem(
                "💡 Mẹo Chơi Hiệu Quả",
                "Những mẹo giúp bạn chơi tốt hơn",
                createTipsContent()
        ));

        // Tab 4: Điều Khiển
        items.add(new GuideItem(
                "🎛️ Hướng Dẫn Điều Khiển",
                "Cách sử dụng các nút điều khiển",
                createControlsContent()
        ));

        return items;
    }

    private String createGameplayContent() {
        return "🚗 BƯỚC 1: CHỌN XE ĐẶT CƯỢC\n" +
                "• Chọn xe muốn đặt cược (Xe Đỏ, Xanh, Vàng, v.v.)\n" +
                "• Nhập số tiền muốn cược vào ô tương ứng\n" +
                "• Có thể đặt cược nhiều xe cùng lúc\n\n" +

                "💰 BƯỚC 2: ĐẶT TIỀN CƯỢC\n" +
                "• Số tiền tối thiểu: 100 VND\n" +
                "• Kiểm tra số dư tài khoản\n" +
                "• Xác nhận số tiền cược\n\n" +

                "🏁 BƯỚC 3: BẮT ĐẦU ĐUA\n" +
                "• Nhấn nút 'Bắt Đầu Đua'\n" +
                "• Quan sát các xe chạy đua\n" +
                "• Chờ kết quả cuộc đua\n\n" +

                "🏆 BƯỚC 4: NHẬN KẾT QUẢ\n" +
                "• Xe về đích đầu tiên sẽ thắng\n" +
                "• Nhận tiền thưởng nếu đoán đúng\n" +
                "• Xem thống kê chi tiết";
    }

    private String createRulesContent() {
        return "💵 QUY ĐỊNH TIỀN CƯỢC:\n" +
                "• Số tiền cược tối thiểu: 100 VND\n" +
                "• Không giới hạn số tiền cược tối đa\n" +
                "• Có thể cược nhiều xe trong 1 ván\n" +
                "• Tiền cược sẽ bị trừ ngay khi đặt\n\n" +

                "🎯 TỶ LỆ TRÚNG THƯỞNG:\n" +
                "• Cược 1 xe: x2.0 tiền cược (nếu thắng)\n" +
                "• Cược nhiều xe: chia tỷ lệ tương ứng\n" +
                "• Không có hòa trong game này\n\n" +

                "⚠️ LƯU Ý QUAN TRỌNG:\n" +
                "• Phải có đủ tiền trong tài khoản\n" +
                "• Không thể hủy cược sau khi đặt\n" +
                "• Kết quả dựa trên hệ thống random\n" +
                "• Mỗi ván chơi độc lập với nhau\n\n" +

                "🔄 NẠP TIỀN & RÚT TIỀN:\n" +
                "• Nạp tiền: Nhấn nút 'Nạp Tiền'\n" +
                "• Không mất phí giao dịch\n" +
                "• Tiền được cập nhật ngay lập tức";
    }

    private String createTipsContent() {
        return "🧠 CHIẾN THUẬT CƠ BẢN:\n" +
                "• Không nên cược hết tiền vào 1 xe\n" +
                "• Chia đều cược để tăng cơ hội thắng\n" +
                "• Quan sát kết quả các ván trước\n" +
                "• Đặt mục tiêu lợi nhuận hợp lý\n\n" +

                "📊 QUẢN LÝ VỐN:\n" +
                "• Chỉ chơi với số tiền có thể mất\n" +
                "• Dừng chơi khi đạt mục tiêu\n" +
                "• Không chơi khi tâm lý không ổn định\n" +
                "• Dành 70% vốn chơi, 30% dự phòng\n\n" +

                "🎲 PHÂN TÍCH XÁC SUẤT:\n" +
                "• Mỗi xe có cơ hội thắng như nhau\n" +
                "• Kết quả hoàn toàn ngẫu nhiên\n" +
                "• Không có xe 'may mắn' cố định\n" +
                "• Đừng tin vào 'cảm giác' quá mức\n\n" +

                "⏰ THỜI GIAN CHƠI:\n" +
                "• Chơi trong thời gian giới hạn\n" +
                "• Nghỉ ngơi sau mỗi 30 phút\n" +
                "• Không chơi khi mệt mỏi\n" +
                "• Ưu tiên giải trí, không áp lực";
    }

    private String createControlsContent() {
        return "🎮 CÁC NÚT ĐIỀU KHIỂN:\n\n" +

                "🏁 NÚT 'BẮT ĐẦU ĐUA':\n" +
                "• Bắt đầu cuộc đua mới\n" +
                "• Chỉ hoạt động khi đã đặt cược\n" +
                "• Không thể nhấn khi đang đua\n\n" +

                "🔄 NÚT 'RESET':\n" +
                "• Đặt lại vị trí tất cả xe\n" +
                "• Xóa kết quả ván trước\n" +
                "• Không ảnh hưởng đến tiền cược\n\n" +

                "💰 NÚT 'NẠP TIỀN':\n" +
                "• Thêm tiền vào tài khoản\n" +
                "• Mỗi lần nạp: +5000 VND\n" +
                "• Không giới hạn số lần nạp\n\n" +

                "🔊 ĐIỀU KHIỂN ÂM THANH:\n" +
                "• 🎵: Bật/tắt nhạc nền\n" +
                "• 🔊: Bật/tắt hiệu ứng âm thanh\n" +
                "• Cài đặt được lưu tự động\n\n" +

                "📊 XEM THỐNG KÊ:\n" +
                "• Tab 'Thống Kê': Xem lịch sử chơi\n" +
                "• Tỷ lệ thắng/thua chi tiết\n" +
                "• Tổng tiền thắng/thua\n\n" +

                "🚪 ĐĂNG XUẤT:\n" +
                "• Lưu dữ liệu tự động\n" +
                "• Trở về màn hình đăng nhập\n" +
                "• Dữ liệu không bị mất";
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    // Inner class cho GuideItem
    public static class GuideItem {
        private String title;
        private String subtitle;
        private String content;

        public GuideItem(String title, String subtitle, String content) {
            this.title = title;
            this.subtitle = subtitle;
            this.content = content;
        }

        // Getters
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public String getContent() { return content; }
    }
}
