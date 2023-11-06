module and_gate(a, b, out);
  input wire a, b;
  output wire out;

  wire nand_out;

  nand_gate nand_gate1(a, b, nand_out);
  not_gate not_gate1(nand_out, out);
endmodule

module or_gate(a, b, out);
  input wire a, b;
  output wire out;
  
  wire nor_out;

  nor_gate nor_gate1(a, b, nor_out);
  not_gate not_gate1(nor_out, out);
endmodule

module xor_gate(a, b, out);
  input wire a, b;
  output out;

  supply1 pwr;
  supply0 gnd;

  wire pmos1_out;
  pmos pmos1(pmos1_out, pwr, a);
  nmos nmos1(out, pmos1_out, b);

  wire pmos2_out;
  nmos nmos2(nmos2_out, pwr, a);
  pmos pmos2(out, nmos2_out, b);

  wire pmos3_out;
  pmos pmos3(pmos3_out, gnd, a);
  pmos pmos4(out, pmos3_out, b);

  wire nmos3_out;
  nmos nmos3(nmos3_out, gnd, a);
  nmos nmos4(out, nmos3_out, b);

endmodule
  
module nor_gate(a, b, out);
  input wire a, b;
  output out;

  supply1 pwr;
  supply0 gnd;

  wire pmos1_out;

  pmos pmos1(pmos1_out, pwr, a);
  pmos pmos2(out, pmos1_out, b);

  nmos nmos1(out, gnd, a);
  nmos nmos2(out, gnd, b);
endmodule

module nand_gate(a, b, out);
  input wire a, b;
  output out;

  supply1 pwr;
  supply0 gnd;

  wire nmos1_out;

  pmos pmos_a(out, pwr, a);
  pmos pmos_b(out, pwr, b);

  nmos nmos_a(nmos1_out, gnd, b);
  nmos nmos_b(out, nmos1_out, a);
endmodule



module not_gate(a, out);
  input wire a;
  output out;

  supply1 pwr;
  supply0 gnd;

  pmos pmos1(out, pwr, a);
  nmos nmos1(out, gnd, a);
endmodule

module median_gate(a, b, c, out);
  input wire a, b, c;
  output wire out;
  
  wire and_ab_out;
  wire and_bc_out;
  wire and_ac_out;

  and_gate and_ab(a, b, and_ab_out);
  and_gate and_bc(b, c, and_bc_out);
  and_gate and_ac(a, c, and_ac_out);

  wire or_12_out;

  or_gate or_12(and_ab_out, and_bc_out, or_12_out);
  or_gate or_123(or_12_out, and_ac_out, out);
endmodule

module full_adder(a, b, c_in, s, c_out);
  input wire a, b, c_in;
  output wire s, c_out;

  median_gate median(a, b, c_in, c_out);

  wire xor_ab_out;
  xor_gate xor_ab(a, b, xor_ab_out);
  xor_gate xor_all(xor_ab_out, c_in, s);
endmodule

module adder_4bit(a, b, c_in, out, overflow);
  input [3:0] a, b;
  input c_in;
  output [3:0] out;
  output overflow;

  wire c_out_1; // нулевой перенос
  wire c_out_2;
  wire c_out_3;
  wire c_out_4; // бит переполнения

  full_adder first_bit(a[0], b[0], c_in, out[0], c_out_1);
  full_adder second_bit(a[1], b[1], c_out_1, out[1], c_out_2);
  full_adder third_bit(a[2], b[2], c_out_2, out[2], c_out_3);
  full_adder fourth_bit(a[3], b[3], c_out_3, out[3], overflow);
endmodule

module and_4bit(a, b, out);
  input [3:0] a, b;
  output [3:0] out;

  and_gate and_1(a[0], b[0], out[0]);
  and_gate and_2(a[1], b[1], out[1]);
  and_gate and_3(a[2], b[2], out[2]);
  and_gate and_4(a[3], b[3], out[3]);
endmodule 

module or_4bit(a, b, out);
  input [3:0] a, b;
  output [3:0] out;

  or_gate or_1(a[0], b[0], out[0]);
  or_gate or_2(a[1], b[1], out[1]);
  or_gate or_3(a[2], b[2], out[2]);
  or_gate or_4(a[3], b[3], out[3]);
endmodule 

module not_4bit(a, out);
  input [3:0] a;
  output [3:0] out;

  not_gate not_1(a[0], out[0]);
  not_gate not_2(a[1], out[1]);
  not_gate not_3(a[2], out[2]);
  not_gate not_4(a[3], out[3]);
endmodule 

module choose_1bit_1control(a, b, control, out);
/*
control - 1: a
control - 0: b
*/
  input a, b, control;
  output out;
  wire not_control;
  not_gate not_c(control, not_control);
  wire a_out, b_out;
  and_gate aa(control, a, a_out);
  and_gate ba(not_control, b, b_out);
  or_gate outp(a_out, b_out, out);
endmodule

module choose_4bit_1control(a, b, control, out);
  input [3:0] a, b;
  input control;
  output [3:0] out;

  choose_1bit_1control out_3(a[3], b[3], control, out[3]);
  choose_1bit_1control out_2(a[2], b[2], control, out[2]);
  choose_1bit_1control out_1(a[1], b[1], control, out[1]);
  choose_1bit_1control out_0(a[0], b[0], control, out[0]);
endmodule

module choose_4bit_2control(a_11, a_10, a_01, a_00, control_1, control_0, out);
  input [3:0] a_11, a_10, a_01, a_00;
  input control_1, control_0;
  output [3:0] out;

  wire [3:0] out_0;
  choose_4bit_1control out0(a_01, a_00, control_0, out_0);
  wire [3:0] out_1;
  choose_4bit_1control out1(a_11, a_10, control_0, out_1);

  choose_4bit_1control out2(out_1, out_0, control_1, out);
endmodule

module slt(a, b, out);
  input [3:0] a, b;
  output [3:0] out;

  supply1 pwr;
  supply0 gnd;
  
  not_gate not3(pwr, out[3]);
  not_gate not2(pwr, out[2]);
  not_gate not1(pwr, out[1]);

  wire [3:0] not_b;
  wire overflow;
  wire [3:0] minus;
  not_4bit not_4(b, not_b);
  adder_4bit add(a, not_b, pwr, minus, overflow);

  wire out_00;
  wire out_01;
  wire out_10;
  wire out_11;
  not_gate out01(pwr, out_01);
  not_gate out10(gnd, out_10);

  wire first;
  wire second;
  wire res;
  choose_1bit_1control f(minus[3], out_10, b[3], first);
  choose_1bit_1control s(out_01, minus[3], b[3], second);
  choose_1bit_1control r(first, second, a[3], out[0]);
endmodule

module alu(a, b, control, res);
  input [3:0] a, b; // Операнды
  input [2:0] control; // Управляющие сигналы для выбора операции

  output [3:0] res;

  wire [3:0] out_000;
  wire [3:0] out_001;
  wire [3:0] out_010;
  wire [3:0] out_011;
  wire [3:0] out_100;
  wire [3:0] out_101;
  wire [3:0] out_110;
  wire [3:0] out_111;

  wire [3:0] out_00;
  wire [3:0] out_01;
  wire [3:0] out_10;
  wire [3:0] out_11;


  reg k = 0;
  reg r = 1;

  wire not_control_0;
  not_gate not_c1(control[0], not_control_0);

  // "and" part
  and_4bit control_000(a, b, out_000);
  not_4bit control_001(out_000, out_001);
  choose_4bit_1control control_00(out_001, out_000, control[0], out_00);

  // "or" part
  or_4bit control_010(a, b, out_010);
  not_4bit control_011(out_010, out_011);
  choose_4bit_1control control_01(out_011, out_010, control[0], out_01);

  // "sum" part
  wire [3:0] not_b;
  not_4bit not_b1(b, not_b);
  wire [3:0] b_or_not_b;
  wire overflow_100;  
  choose_4bit_1control control_10(not_b, b, control[0], b_or_not_b);
  adder_4bit control_100(a, b_or_not_b, control[0], out_10, overflow_100);
  
  slt control_11(a, b, out_11);

  choose_4bit_2control result(out_11, out_10, out_01, out_00, control[2], control[1], res);

endmodule


module d_latch(clk, d, we, q);
  input clk; // Сигнал синхронизации
  input d; // Бит для записи в ячейку
  input we; // Необходимо ли перезаписать содержимое ячейки

  output reg q; // Сама ячейка
  // Изначально в ячейке хранится 0
  initial begin
    q <= 0;
  end
  // Значение изменяется на переданное на спаде сигнала синхронизации
  always @ (negedge clk) begin
    // Запись происходит при we = 1
    if (we) begin
      q <= d;
    end
  end
endmodule


module register_4bit(clk, d, we, q);
  input clk; // Сигнал синхронизации
  input [3:0] d; // Бит для записи в ячейку (4 бит)
  input we; // Необходимо ли перезаписать содержимое ячейки (1 бит)

  output [3:0] q; // Сама ячейка (4 бит)

  d_latch d0(clk, d[0], we, q[0]);
  d_latch d1(clk, d[1], we, q[1]);
  d_latch d2(clk, d[2], we, q[2]);
  d_latch d3(clk, d[3], we, q[3]);
endmodule


module register_file(clk, rd_addr, we_addr, we_data, rd_data, we);
  input clk; // Сигнал синхронизации
  input [1:0] rd_addr, we_addr; // Номера регистров для чтения и записи
  input [3:0] we_data; // Данные для записи в регистровый файл
  input we; // Необходимо ли перезаписать содержимое регистра

  output [3:0] rd_data; // Изменил на reg

  wire [3:0] registers_read [0:3]; // Создаем массив регистров
  wire  we_3, we_2, we_1, we_0;

  wire not1;
  wire not0;
  not_gate nr1(we_addr[1], not1);
  not_gate nr0(we_addr[0], not0);
 

  wire and_3;
  and_gate and_31(we_addr[1], we_addr[0], and_3);
  and_gate and_32(and_3, we, we_3);

  wire and_2;
  and_gate and_21(we_addr[1], not0, and_2);
  and_gate and_22(and_2, we, we_2);

  wire and_1;
  and_gate and_11(not1, we_addr[0], and_1);
  and_gate and_12(and_1, we, we_1);

  wire and_0;
  and_gate and_01(not1, not0, and_0);
  and_gate and_02(and_0, we, we_0);
  

  register_4bit D00 (clk, we_data, we_0, registers_read[0]);
  register_4bit D01 (clk, we_data, we_1, registers_read[1]);
  register_4bit D10 (clk, we_data, we_2, registers_read[2]);
  register_4bit D11 (clk, we_data, we_3, registers_read[3]);

  choose_4bit_2control read(registers_read[3], registers_read[2], registers_read[1], registers_read[0], rd_addr[1], rd_addr[0], rd_data);
endmodule

module counter(clk, addr, control, immediate, data);
  input clk; // Сигнал синхронизации
  input [1:0] addr; // Номер значения счетчика которое читается или изменяется
  input [3:0] immediate; // Целочисленная константа, на которую увеличивается/уменьшается значение счетчика
  input control; // 0 - операция инкремента, 1 - операция декремента
  output [3:0] data; // Данные из значения под номером addr, подающиеся на выход
  supply1 pwr;
  wire [3:0] reg2alu, alu2reg;
  wire [2:0] control_alu;
  assign control_alu[2] = 1;
  assign control_alu[1] = 0;
  assign control_alu[0] = control;

  alu ALU(reg2alu, immediate, control_alu, alu2reg);

  register_file reg1(clk, addr, addr, alu2reg, reg2alu, pwr);
  assign data = reg2alu;
endmodule