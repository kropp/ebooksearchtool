
from south.db import db
from django.db import models
from server.book.models import *

class Migration:
    
    def forwards(self, orm):
        
        # Adding model 'Alias'
        db.create_table('book_alias', (
            ('id', orm['book.Alias:id']),
            ('name', orm['book.Alias:name']),
        ))
        db.send_create_signal('book', ['Alias'])
        
        # Adding model 'Author'
        db.create_table('book_author', (
            ('id', orm['book.Author:id']),
            ('name', orm['book.Author:name']),
        ))
        db.send_create_signal('book', ['Author'])
        
        # Adding model 'Annotation'
        db.create_table('book_annotation', (
            ('id', orm['book.Annotation:id']),
            ('name', orm['book.Annotation:name']),
        ))
        db.send_create_signal('book', ['Annotation'])
        
        # Adding model 'Series'
        db.create_table('book_series', (
            ('id', orm['book.Series:id']),
            ('name', orm['book.Series:name']),
        ))
        db.send_create_signal('book', ['Series'])
        
        # Adding model 'Book'
        db.create_table('book_book', (
            ('id', orm['book.Book:id']),
            ('title', orm['book.Book:title']),
            ('lang', orm['book.Book:lang']),
        ))
        db.send_create_signal('book', ['Book'])
        
        # Adding model 'BookFile'
        db.create_table('book_bookfile', (
            ('id', orm['book.BookFile:id']),
            ('link', orm['book.BookFile:link']),
            ('size', orm['book.BookFile:size']),
            ('type', orm['book.BookFile:type']),
            ('more_info', orm['book.BookFile:more_info']),
            ('img_link', orm['book.BookFile:img_link']),
        ))
        db.send_create_signal('book', ['BookFile'])
        
        # Adding model 'Tag'
        db.create_table('book_tag', (
            ('id', orm['book.Tag:id']),
            ('name', orm['book.Tag:name']),
        ))
        db.send_create_signal('book', ['Tag'])
        
        # Adding ManyToManyField 'Author.alias'
        db.create_table('book_author_alias', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('author', models.ForeignKey(orm.Author, null=False)),
            ('alias', models.ForeignKey(orm.Alias, null=False))
        ))
        
        # Adding ManyToManyField 'Book.annotation'
        db.create_table('book_book_annotation', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('annotation', models.ForeignKey(orm.Annotation, null=False))
        ))
        
        # Adding ManyToManyField 'Author.tag'
        db.create_table('book_author_tag', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('author', models.ForeignKey(orm.Author, null=False)),
            ('tag', models.ForeignKey(orm.Tag, null=False))
        ))
        
        # Adding ManyToManyField 'Book.series'
        db.create_table('book_book_series', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('series', models.ForeignKey(orm.Series, null=False))
        ))
        
        # Adding ManyToManyField 'Book.book_file'
        db.create_table('book_book_book_file', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('bookfile', models.ForeignKey(orm.BookFile, null=False))
        ))
        
        # Adding ManyToManyField 'Author.book'
        db.create_table('book_author_book', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('author', models.ForeignKey(orm.Author, null=False)),
            ('book', models.ForeignKey(orm.Book, null=False))
        ))
        
        # Adding ManyToManyField 'Book.tag'
        db.create_table('book_book_tag', (
            ('id', models.AutoField(verbose_name='ID', primary_key=True, auto_created=True)),
            ('book', models.ForeignKey(orm.Book, null=False)),
            ('tag', models.ForeignKey(orm.Tag, null=False))
        ))
        
    
    
    def backwards(self, orm):
        
        # Deleting model 'Alias'
        db.delete_table('book_alias')
        
        # Deleting model 'Author'
        db.delete_table('book_author')
        
        # Deleting model 'Annotation'
        db.delete_table('book_annotation')
        
        # Deleting model 'Series'
        db.delete_table('book_series')
        
        # Deleting model 'Book'
        db.delete_table('book_book')
        
        # Deleting model 'BookFile'
        db.delete_table('book_bookfile')
        
        # Deleting model 'Tag'
        db.delete_table('book_tag')
        
        # Dropping ManyToManyField 'Author.alias'
        db.delete_table('book_author_alias')
        
        # Dropping ManyToManyField 'Book.annotation'
        db.delete_table('book_book_annotation')
        
        # Dropping ManyToManyField 'Author.tag'
        db.delete_table('book_author_tag')
        
        # Dropping ManyToManyField 'Book.series'
        db.delete_table('book_book_series')
        
        # Dropping ManyToManyField 'Book.book_file'
        db.delete_table('book_book_book_file')
        
        # Dropping ManyToManyField 'Author.book'
        db.delete_table('book_author_book')
        
        # Dropping ManyToManyField 'Book.tag'
        db.delete_table('book_book_tag')
        
    
    
    models = {
        'book.alias': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.annotation': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.TextField', [], {'max_length': '10000'})
        },
        'book.author': {
            'alias': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Alias']"}),
            'book': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Book']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']"})
        },
        'book.book': {
            'annotation': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Annotation']"}),
            'book_file': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.BookFile']"}),
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'lang': ('django.db.models.fields.CharField', [], {'max_length': '2'}),
            'series': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Series']"}),
            'tag': ('django.db.models.fields.related.ManyToManyField', [], {'to': "orm['book.Tag']"}),
            'title': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.bookfile': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'img_link': ('django.db.models.fields.URLField', [], {'max_length': '200', 'null': 'True'}),
            'link': ('django.db.models.fields.URLField', [], {'max_length': '200', 'unique': 'True'}),
            'more_info': ('django.db.models.fields.TextField', [], {'max_length': '10000', 'null': 'True'}),
            'size': ('django.db.models.fields.IntegerField', [], {}),
            'type': ('django.db.models.fields.CharField', [], {'max_length': '10'})
        },
        'book.series': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        },
        'book.tag': {
            'id': ('django.db.models.fields.AutoField', [], {'primary_key': 'True'}),
            'name': ('django.db.models.fields.CharField', [], {'max_length': '255', 'unique': 'True'})
        }
    }
    
    complete_apps = ['book']
