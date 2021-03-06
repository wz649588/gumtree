/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.gen.ruby;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.github.gumtreediff.gen.SyntaxException;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Symbol;
import com.github.gumtreediff.tree.TreeContext;
import org.jrubyparser.ast.NodeType;
import org.junit.Test;

import static com.github.gumtreediff.tree.Symbol.symbol;
import static org.junit.Assert.*;

public class TestRubyGenerator {

    private static final Symbol ROOT_NODE = symbol(NodeType.ROOTNODE.name());

    @Test
    public void testFileParsing() throws IOException {
        ITree tree = new RubyTreeGenerator().generateFrom().
                charset("UTF-8").stream(getClass().getResourceAsStream("/sample.rb")).getRoot();
        assertEquals(ROOT_NODE, tree.getType());
        assertEquals(1726, tree.getSize());
    }

    @Test
    public void testSimpleSyntax() throws IOException {
        String input = "module Foo; puts \"Hello world!\"; end;";
        ITree t = new RubyTreeGenerator().generateFrom().string(input).getRoot();
        assertEquals(ROOT_NODE, t.getType());
    }

    @Test
    public void testRuby2Syntax() throws IOException {
        String input = "{ foo: true }";
        ITree t = new RubyTreeGenerator().generateFrom().string(input).getRoot();
        assertEquals(ROOT_NODE, t.getType());
    }

    @Test
    public void testPosition() throws IOException {
        String input = "module Baz\nclass Foo\n\tdef foo(bar)\n\t\tputs bar\n\tend\nend\nend";
        TreeContext ctx = new RubyTreeGenerator().generateFrom().string(input);
        ITree root = ctx.getRoot();
    }

    @Test(expected = SyntaxException.class)
    public void badSyntax() throws IOException {
        String input = "module Foo\ndef foo((bar)\n\tputs 'foo'\nend\n";
        TreeContext ct = new RubyTreeGenerator().generateFrom().string(input);
    }

}
