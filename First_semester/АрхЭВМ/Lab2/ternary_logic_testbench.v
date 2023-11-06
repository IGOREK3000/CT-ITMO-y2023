`include "templates.v"

module testbench();

  reg x = 0;
  reg y = 0;
  reg w = 0;
  reg v = 0;

  reg z = 0;
  wire and_out;
  wire or_out;
//  wire [1:0] c; // Изменили тип порта c на wire
  wire and_result;
  wire nand_result;
  wire not_out;
  wire clos_out;
  wire not_v_out;
  wire not_y_out;
  wire s;
  wire c_out;
  not_gate not_v(v, not_v_out);
  not_gate not_y(y, not_y_out);
 // and_4_gate clos_h(x, not_y_out, w, not_v_out, clos_out);

//  and_4_gate nand_xy(x, y, w, v, z);
  median_gate median_xy(x, y, z, or_out);
//  and_gate and_xy(x, y, and_out);
  not_gate not_x(x, not_out);
  xor_gate and_xy(x, y, and_out);
  full_adder full_adder(x, y, z, s, c_out);
  initial begin
    
    $display("full_adder: ");
    #5 x = 0; y = 0; z = 0;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 0; y = 0; z = 1;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 0; y = 1; z = 0;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 0; y = 1; z = 1;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 1; y = 0; z = 0;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 1; y = 0; z = 1;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 1; y = 1; z = 0;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    #5 x = 1; y = 1; z = 1;
    $display("x = %b, y = %b,z = %b, s = %b, c_out = %b", x, y, z, s, c_out);
    $display("median_gate: ");
    #5 x = 0; y = 0;
    $display("x = %b, y = %b,z = %b", x, y, and_out);
    #5 x = 0; y = 1;
    $display("x = %b, y = %b,z = %b", x, y, and_out);
    #5 x = 1; y = 0;
    $display("x = %b, y = %b,z = %b", x, y, and_out);
    #5 x = 1; y = 1;
    $display("x = %b, y = %b,z = %b", x, y, and_out);
    $display("not_gate: ");
    #5 x = 0; 
    $display("x = %b,z = %b", x, not_out);
    #5 x = 1; 
    $display("x = %b,z = %b", x, not_out);
  

  end
endmodule