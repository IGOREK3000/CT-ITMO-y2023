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


module and_4_gate(a, b, c, d, out);
  input wire a, b, c, d;
  output out;

  supply1 pwr;
  supply0 gnd;

  wire nmos1_out;
  wire nmos2_out;
  wire nmos3_out;

  pmos pmos1(out, gnd, a);
  pmos pmos2(out, gnd, b);
  pmos pmos3(out, gnd, c);
  pmos pmos4(out, gnd, d);

  nmos nmos1(nmos1_out, pwr, a);
  nmos nmos2(nmos2_out, nmos1_out, b);
  nmos nmos3(nmos3_out, nmos2_out, c);
  nmos nmos4(out, nmos3_out, d);

endmodule

module or_4_gate(a, b, c, d, out);
  input wire a, b, c, d;
  output out;

  supply1 pwr;
  supply0 gnd;

  wire pmos1_out;
  wire pmos2_out;
  wire pmos3_out;

  pmos pmos1(pmos1_out, gnd, a);
  pmos pmos2(pmos2_out, pmos1_out, b);
  pmos pmos3(pmos3_out, pmos2_out, c);
  pmos pmos4(out, pmos3_out, d);

  nmos nmos1(out, pwr, a);
  nmos nmos2(out, pwr, b);
  nmos nmos3(out, pwr, c);
  nmos nmos4(out, pwr, d);

endmodule


module not_gate(a, out);
  input wire a;
  output out;

  supply1 pwr;
  supply0 gnd;

  pmos pmos1(out, pwr, a);
  nmos nmos1(out, gnd, a);
endmodule


module ternary_min(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire [1:0] outwire; 

  wire not_a0_wire;
  wire not_a1_wire;
  wire not_b0_wire;
  wire not_b1_wire;

  not_gate not_a0(a[0], not_a0_wire);
  not_gate not_a1(a[1], not_a1_wire);
  not_gate not_b0(b[0], not_b0_wire);
  not_gate not_b1(b[1], not_b1_wire);

  //1-ый бит 
  and_4_gate clos_out0_1(a[1], not_a0_wire, b[1], not_b0_wire, out[1]);

  //2-ой бит
  wire clos1_out;
  wire clos2_out;
  wire clos3_out;
  wire or_out;

  and_4_gate clos_out1_1(not_a1_wire, a[0], not_b1_wire, b[0], clos1_out);
  and_4_gate clos_out1_2(not_a1_wire, a[0], b[1], not_b0_wire, clos2_out);
  and_4_gate clos_out1_3(a[1], not_a0_wire, not_b1_wire, b[0], clos3_out);

  or_gate clos1_or_clos2(clos1_out, clos2_out, or_out);
  or_gate prev_or_clos3(or_out, clos3_out, out[0]);

endmodule

module ternary_max(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire not_a0_wire;
  wire not_a1_wire;
  wire not_b0_wire;
  wire not_b1_wire;

  not_gate not_a0(a[0], not_a0_wire);
  not_gate not_a1(a[1], not_a1_wire);
  not_gate not_b0(b[0], not_b0_wire);
  not_gate not_b1(b[1], not_b1_wire);

  //1-ый бит 
  wire clos_out1_1_out;
  wire clos_out1_2_out;
  wire clos_out1_3_out;
  wire clos_out1_4_out;
  wire clos_out1_5_out;
  
  and_4_gate clos_out1_1(not_a1_wire, not_a0_wire, b[1], not_b0_wire, clos_out1_1_out);
  and_4_gate clos_out1_2(not_a1_wire, a[0], b[1], not_b0_wire, clos_out1_2_out);
  and_4_gate clos_out1_3(a[1], not_a0_wire, not_b1_wire, not_b0_wire, clos_out1_3_out);
  and_4_gate clos_out1_4(a[1], not_a0_wire, not_b1_wire, b[0], clos_out1_4_out);
  and_4_gate clos_out1_5(a[1], not_a0_wire, b[1], not_b0_wire, clos_out1_5_out);

  wire or_out1_12_out;
  wire or_out1_123_out;
  wire or_out1_1234_out;

  or_gate or_out1_12(clos_out1_1_out, clos_out1_2_out, or_out1_12_out);
  or_gate or_out1_123(or_out1_12_out, clos_out1_3_out, or_out1_123_out);
  or_gate or_out1_1234(or_out1_123_out, clos_out1_4_out, or_out1_1234_out);
  or_gate or_out1_12345(or_out1_1234_out, clos_out1_5_out, out[1]);
  
  //2-ой бит
  wire clos_out0_1_out;
  wire clos_out0_2_out;
  wire clos_out0_3_out;
  //клозы 
  and_4_gate clos_out0_1(not_a1_wire, not_a0_wire, not_b1_wire, b[0], clos_out0_1_out);
  and_4_gate clos_out0_2(not_a1_wire, a[0], not_b1_wire, not_b0_wire, clos_out0_2_out);
  and_4_gate clos_out0_3(not_a1_wire, a[0], not_b1_wire, b[0], clos_out0_3_out);

  wire or_out0_12_out;

  or_gate or_out0_12(clos_out0_1_out, clos_out0_2_out, or_out0_12_out);
  or_gate or_out0_123(or_out0_12_out, clos_out0_3_out, out[0]);

endmodule

module ternary_any(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire not_a0_wire;
  wire not_a1_wire;
  wire not_b0_wire;
  wire not_b1_wire;

  not_gate not_a0(a[0], not_a0_wire);
  not_gate not_a1(a[1], not_a1_wire);
  not_gate not_b0(b[0], not_b0_wire);
  not_gate not_b1(b[1], not_b1_wire);

  //1-ый бит 
  wire clos_out1_1_out;
  wire clos_out1_2_out;
  wire clos_out1_3_out;
  
  and_4_gate clos_out1_1(a[1], not_a0_wire, b[1], not_b0_wire, clos_out1_1_out);
  and_4_gate clos_out1_2(not_a1_wire, a[0], b[1], not_b0_wire, clos_out1_2_out);
  and_4_gate clos_out1_3(a[1], not_a0_wire, not_b1_wire, b[0], clos_out1_3_out);

  wire or_out1_12_out;

  or_gate or_out1_12(clos_out1_1_out, clos_out1_2_out, or_out1_12_out);
  or_gate or_out1_123(or_out1_12_out, clos_out1_3_out, out[1]);
  
  //2-ой бит
  wire clos_out0_1_out;
  wire clos_out0_2_out;
  wire clos_out0_3_out;
  //клозы 
  and_4_gate clos_out0_1(not_a1_wire, not_a0_wire, b[1], not_b0_wire, clos_out0_1_out);
  and_4_gate clos_out0_2(not_a1_wire, a[0], not_b1_wire, b[0], clos_out0_2_out);
  and_4_gate clos_out0_3(a[1], not_a0_wire, not_b1_wire, not_b0_wire, clos_out0_3_out);

  wire or_out0_12_out;

  or_gate or_out0_12(clos_out0_1_out, clos_out0_2_out, or_out0_12_out);
  or_gate or_out0_123(or_out0_12_out, clos_out0_3_out, out[0]);

  
endmodule

module ternary_consensus(a, b, out);
  input [1:0] a;
  input [1:0] b;
  output [1:0] out;

  wire not_a0_wire;
  wire not_a1_wire;
  wire not_b0_wire;
  wire not_b1_wire;

  not_gate not_a0(a[0], not_a0_wire);
  not_gate not_a1(a[1], not_a1_wire);
  not_gate not_b0(b[0], not_b0_wire);
  not_gate not_b1(b[1], not_b1_wire);

  //1-ый бит сднф
  and_4_gate clos_out1_1(a[1], not_a0_wire, b[1], not_b0_wire, out[1]);

  //2-ой бит скнф
  wire clos_out0_1_out;
  wire clos_out0_2_out;

  or_4_gate clos_out0_1(a[1], a[0], b[1], b[0], clos_out0_1_out);
  or_4_gate clos_out0_2(not_a1_wire, a[0], not_b1_wire, b[0], clos_out0_2_out);

  and_gate and_out0_12(clos_out0_1_out, clos_out0_2_out, out[0]);
endmodule