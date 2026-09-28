import javax.swing.SwingUtilities; // 1. بنستدعي الشاشة الرئيسية اللي عملناها في فولدر ui
import ui.MainDashboard; // 2. مكتبة بتضمن إن الـ GUI يفتح بسلاسة ومن غير تهنيج

public class Main {
    public static void main(String[] args) {
        
        // 3. السطر ده وظيفته يشغل الواجهة في "Thread" منفصل عشان البرنامج يكون سريع
        SwingUtilities.invokeLater(() -> {
            
            // 4. بنعمل نسخة (Object) من الشاشة الرئيسية اللي فيها الأزرار
            MainDashboard dashboard = new MainDashboard();
            
            // 5. السطر ده هو اللي بيخلي الشاشة تظهر فعلياً (لو مش موجود البرنامج هيشتغل في الخلفية بس مش هتشوفيه)
            dashboard.setVisible(true);
            
        });
    }
}