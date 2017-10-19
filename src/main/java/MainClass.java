import unicorn.Unicorn;
import unicorn.UnicornConst;
import unicorn.UnicornException;
import unicorn.X86_MMR;

public class MainClass {
    static void test_x86_mmr() {
        // Initialize emulator in X86-32bit mode
        Unicorn uc;
        try {
            uc = new Unicorn(Unicorn.UC_ARCH_X86, Unicorn.UC_MODE_32);
        } catch (UnicornException uex) {
            System.out.println("Failed on uc_open() with error returned: " + uex);
            return;
        }

        // map 4k
        uc.mem_map(0x400000, 0x1000, Unicorn.UC_PROT_ALL);

        X86_MMR ldtr1 = new X86_MMR(0x1111111122222222L, 0x33333333, 0x44444444, (short)0x5555);
        X86_MMR ldtr2;
        X86_MMR gdtr1 = new X86_MMR(0x6666666677777777L, 0x88888888, 0x99999999, (short)0xaaaa);
        X86_MMR gdtr2, gdtr3, gdtr4;

        int eax;

        // initialize machine registers

        uc.reg_write(Unicorn.UC_X86_REG_LDTR, ldtr1);
        uc.reg_write(Unicorn.UC_X86_REG_GDTR, gdtr1);
        uc.reg_write(Unicorn.UC_X86_REG_EAX, new Long(0xdddddddd));

        // read the registers back out
        eax = (int)((Long)uc.reg_read(Unicorn.UC_X86_REG_EAX)).longValue();
        ldtr2 = (X86_MMR)uc.reg_read(Unicorn.UC_X86_REG_LDTR);
        gdtr2 = (X86_MMR)uc.reg_read(Unicorn.UC_X86_REG_GDTR);

        System.out.printf(">>> EAX = 0x%x\n", eax);

        System.out.printf(">>> LDTR.base = 0x%x\n", ldtr2.base);
        System.out.printf(">>> LDTR.limit = 0x%x\n", ldtr2.limit);
        System.out.printf(">>> LDTR.flags = 0x%x\n", ldtr2.flags);
        System.out.printf(">>> LDTR.selector = 0x%x\n\n", ldtr2.selector);

        System.out.printf(">>> GDTR.base = 0x%x\n", gdtr2.base);
        System.out.printf(">>> GDTR.limit = 0x%x\n", gdtr2.limit);

        uc.close();
    }
    public static void main(String[] args){
        test_x86_mmr();
    }
}
